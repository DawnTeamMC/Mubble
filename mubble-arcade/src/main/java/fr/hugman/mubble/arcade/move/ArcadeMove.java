package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import fr.hugman.mubble.arcade.sim.MoveContext;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

/**
 * One state of the arcade movement state machine.
 * <p>
 * A move is code: when it may start, what it does each tick, how it ends. Every number it reads is
 * one of its {@link #params()}, given by the profile running it. Moves are registered in
 * {@code mubble:arcade_move} and grouped by tags such as {@code #mubble:aerial}, which is what
 * sources and unlocks speak about.
 * <p>
 * Each tick, the simulation first asks the current move whether it {@linkplain #exit ends}, then
 * offers every other move, highest {@linkplain Properties#entryPriority priority} first, the chance to
 * {@linkplain #canEnter start}, as long as the current one {@linkplain #allowsInterruption lets it}.
 * Whichever move is current then {@linkplain #tick runs} the physics of the tick. Landing and leaving
 * the ground are reported back after the world moved the player, see {@link #onLand} and
 * {@link #onLeaveGround}.
 */
public abstract class ArcadeMove {
    private final Properties properties;

    protected ArcadeMove(Properties properties) {
        this.properties = properties;
    }

    public ResourceKey<ArcadeMove> key() {
        return ArcadeBuiltInRegistries.ARCADE_MOVE.getResourceKey(this).orElseThrow(() -> new IllegalStateException("Unregistered move " + this.getClass().getSimpleName()));
    }

    public Identifier id() {
        var id = ArcadeBuiltInRegistries.ARCADE_MOVE.getKey(this);
        return id == null ? Identifier.withDefaultNamespace("unregistered") : id;
    }

    public List<MoveParam> params() {
        return this.properties.params;
    }

    public Optional<MoveParam> param(String name) {
        return this.properties.params.stream().filter(param -> param.name().equals(name)).findFirst();
    }

    public Kind kind() {
        return this.properties.kind;
    }

    public boolean isAirborne() {
        return this.properties.kind == Kind.AIR;
    }

    /** Whether the move is always available: the states that are not moves of their own, like falling. */
    public boolean isBase() {
        return this.properties.base;
    }

    /**
     * The move whose access this one follows. A ledge climb is only a part of a ledge grab, a rollout
     * only the end of a dive: denying, forcing or unlocking the parent covers them.
     */
    public ArcadeMove accessRoot() {
        return this.properties.accessParent == null ? this : this.properties.accessParent.get().accessRoot();
    }

    public boolean handsBusyByDefault() {
        return this.properties.handsBusy;
    }

    public int entryPriority() {
        return this.properties.entryPriority;
    }

    /** Whether the generic entry loop may start this move at all; sub-states are only reached from their parent. */
    public boolean isEnteredByItself() {
        return this.properties.entryPriority >= 0;
    }

    /** The hitbox pose the move asks for. A lower one is used when it does not fit. */
    public Pose pose(MoveContext ctx) {
        return Pose.STANDING;
    }

    /** Whether this move may start now. Access and interruption rules were already checked. */
    public boolean canEnter(MoveContext ctx) {
        return false;
    }

    /** Called when the move starts, before its first {@link #tick}. */
    public void enter(MoveContext ctx) {
    }

    /** The physics of one tick: sets the velocity, and the displacement through the context helpers. */
    public abstract void tick(MoveContext ctx);

    /** A move this one turns into on its own before anything else is considered, such as the end of a timed move. */
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        return null;
    }

    /**
     * Whether {@code next} may cut this move short. By default, moves on the ground give way to
     * anything, and moves in the air to the usual aerial follow-ups once they have lasted a few ticks.
     */
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return switch (this.properties.kind) {
            case GROUND -> true;
            case AIR -> next.kind() != Kind.GROUND && ArcadeMoves.isAerialFollowUp(next) && ctx.state().moveTicks >= ctx.cancelWindow(this);
            case ATTACHED -> false;
        };
    }

    /** The move to land into. By default, the landing state of the ground. */
    @Nullable
    public ArcadeMove onLand(MoveContext ctx) {
        return ctx.landingMove();
    }

    /** The move to fall into when the ground disappears under this one. */
    @Nullable
    public ArcadeMove onLeaveGround(MoveContext ctx) {
        return this.properties.kind == Kind.GROUND ? ArcadeMoves.FALL : null;
    }

    /** Called when the head hits a ceiling. */
    public void onBonk(MoveContext ctx) {
        ctx.state().vy = Math.min(ctx.state().vy, 0.0D);
    }

    /** Food exhaustion per block travelled during the move, when the profile charges for its moves. */
    public double exhaustionPerBlock(MoveContext ctx) {
        return 0.0D;
    }

    /** Whether the landing at the end of this move takes no fall damage. */
    public boolean negatesFallDamage(MoveContext ctx) {
        return false;
    }

    @Override
    public String toString() {
        return this.id().toString();
    }

    /** Where a move happens, which decides the transitions it goes through by default. */
    public enum Kind {
        /** On the ground: leaving it falls. */
        GROUND,
        /** In the air: landing goes back to the ground. */
        AIR,
        /** Held by something: a ledge, a wall. Only the move itself decides how it ends. */
        ATTACHED
    }

    public static final class Properties {
        private Kind kind = Kind.GROUND;
        private boolean base;
        @Nullable
        private Supplier<ArcadeMove> accessParent;
        private boolean handsBusy;
        private int entryPriority = -1;
        private List<MoveParam> params = List.of();

        public static Properties of(Kind kind) {
            var properties = new Properties();
            properties.kind = kind;
            return properties;
        }

        public Properties base() {
            this.base = true;
            return this;
        }

        public Properties accessParent(Supplier<ArcadeMove> parent) {
            this.accessParent = parent;
            return this;
        }

        public Properties handsBusy() {
            this.handsBusy = true;
            return this;
        }

        /** Lets the generic entry loop start the move, the highest priority being tried first. */
        public Properties entryPriority(int priority) {
            this.entryPriority = priority;
            return this;
        }

        public Properties params(MoveParam... params) {
            this.params = List.of(params);
            return this;
        }
    }
}
