package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

/**
 * The roll a dive lands into. Jumping recovers from it right away; otherwise it slows down to a stop.
 */
public class RolloutMove extends ArcadeMove {
    /** How long the rollout lasts. */
    public static final MoveParam TICKS = MoveParam.ticks("ticks", 10.0D);
    /** Speed lost every tick. */
    public static final MoveParam DECEL = MoveParam.speed("decel", 0.03D);

    public RolloutMove(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(MoveContext ctx) {
        ctx.setHorizontal(ctx.velocityYaw(), Math.max(0.0D, ctx.horizontalSpeed() - ctx.param(this, DECEL) * ctx.grip()));
        ctx.gravity();
        ctx.hugGround(0.0D);
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        return ctx.state().moveTicks >= ctx.settings(this).ticks(TICKS) ? ctx.baseGroundMove() : null;
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return next == ArcadeMoves.JUMP;
    }

    @Override
    public Pose pose(MoveContext ctx) {
        return Pose.SWIMMING;
    }
}
