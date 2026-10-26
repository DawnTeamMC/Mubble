package fr.hugman.mubble.platformer.replay;

import fr.hugman.mubble.platformer.sim.PlatformerBody;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import fr.hugman.mubble.platformer.sim.PlatformerSimulation;
import fr.hugman.mubble.platformer.sim.PlatformerState;
import fr.hugman.mubble.platformer.sim.PlatformerTuning;
import fr.hugman.mubble.platformer.sim.PlatformerWorld;
import fr.hugman.mubble.platformer.sim.MoveResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Plays input frames back through the shared simulation, the way either side would.
 */
public final class PlatformerReplayer {
    private PlatformerReplayer() {
    }

    /**
     * One step of a trajectory.
     *
     * @param tick     the tick number of the frame
     * @param position where the step ended
     * @param state    the encoding of the state after the step
     * @param result   what the world did with the step
     */
    public record Point(int tick, Vec3 position, byte[] state, MoveResult result) {
        public boolean sameAs(Point other) {
            return this.tick == other.tick && this.position.equals(other.position) && java.util.Arrays.equals(this.state, other.state);
        }
    }

    /**
     * Plays {@code frames} the way a client predicts: each step moves {@code player} through vanilla
     * collisions.
     */
    public static List<Point> playAsClient(Player player, PlatformerState state, List<PlatformerInputFrame> frames, Supplier<PlatformerTuning> tuning) {
        var world = new PlatformerWorld(player);
        var body = new EntityBody(player);
        var points = new ArrayList<Point>(frames.size());
        for (var frame : frames) {
            var ctx = PlatformerSimulation.step(state, frame, tuning.get(), world, body);
            points.add(new Point(frame.tick(), player.position(), state.toBytes(), ctx.result()));
        }
        return points;
    }

    /**
     * Plays a client trajectory back the way the server validates it: each step is planned from the
     * position the client reported, and settled with the result it reported.
     */
    public static List<Point> playAsServer(Player player, PlatformerState state, List<PlatformerInputFrame> frames, List<Point> client, Vec3 start, Supplier<PlatformerTuning> tuning) {
        var world = new PlatformerWorld(player);
        var points = new ArrayList<Point>(frames.size());
        var position = start;
        for (int i = 0; i < frames.size(); i++) {
            var frame = frames.get(i);
            var result = client.get(i).result();
            var ctx = PlatformerSimulation.plan(state, frame, tuning.get(), world, position);
            PlatformerSimulation.settle(ctx, result);
            position = position.add(result.dx(), result.dy(), result.dz());
            points.add(new Point(frame.tick(), position, state.toBytes(), result));
        }
        return points;
    }

    /** The first step where two trajectories disagree, if any. */
    public static Optional<Integer> firstDivergence(List<Point> a, List<Point> b) {
        int size = Math.min(a.size(), b.size());
        for (int i = 0; i < size; i++) {
            if (!a.get(i).sameAs(b.get(i))) {
                return Optional.of(i);
            }
        }
        return a.size() == b.size() ? Optional.empty() : Optional.of(size);
    }

    /**
     * A player moving through vanilla collisions: the body of a client, and of the tests standing in
     * for one.
     */
    public static final class EntityBody implements PlatformerBody {
        private final Player player;

        public EntityBody(Player player) {
            this.player = player;
        }

        @Override
        public Vec3 position() {
            return this.player.position();
        }

        @Override
        public MoveResult move(Pose pose, Vec3 first, Vec3 second) {
            var before = this.player.position();
            if (this.player.getPose() != pose) {
                this.player.setPose(pose);
            }
            boolean horizontalCollision = false;
            if (first.lengthSqr() > 0.0D) {
                this.player.move(MoverType.SELF, first);
                horizontalCollision = this.player.horizontalCollision;
            }
            this.player.move(MoverType.SELF, second);
            horizontalCollision |= this.player.horizontalCollision;
            var after = this.player.position();
            return new MoveResult(after.x - before.x, after.y - before.y, after.z - before.z, this.player.onGround(), horizontalCollision, this.player.verticalCollision);
        }
    }
}
