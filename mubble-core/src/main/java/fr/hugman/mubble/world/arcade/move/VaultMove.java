package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;
import org.jspecify.annotations.Nullable;

/**
 * Running into an obstacle up to a block high: the player mantles over it without losing speed.
 * Anything vanilla steps up on its own is left to vanilla.
 */
public class VaultMove extends ArcadeMove {
    /** The highest obstacle vaulted over, in blocks. */
    public static final MoveParam MAX_HEIGHT = MoveParam.blocks("max_height", 1.0D);
    /** Share of the run speed the player needs to move at. */
    public static final MoveParam MIN_SPEED = MoveParam.factor("min_speed", 0.5D);
    /** How long the vault lasts, for its animation; the lift itself is immediate. */
    public static final MoveParam TICKS = MoveParam.ticks("ticks", 4.0D);

    public VaultMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        if (!state.grounded || (state.move != ArcadeMoves.RUN && state.move != ArcadeMoves.LAND)) {
            return false;
        }
        double speed = ctx.horizontalSpeed();
        if (speed < ctx.param(this, MIN_SPEED) * ctx.tuning().effectiveRunSpeed() || ctx.stickAlong(state.vx, state.vz) < 0.5D) {
            return false;
        }
        double lift = ctx.liftOver(ctx.param(this, MAX_HEIGHT));
        if (lift <= 0.0D) {
            return false;
        }
        state.anchorY = lift;
        return true;
    }

    @Override
    public void enter(MoveContext ctx) {
        ctx.nudge(0.0D, ctx.state().anchorY, 0.0D);
    }

    @Override
    public void tick(MoveContext ctx) {
        ctx.groundMotion(ctx.stickTargetSpeed(true));
        if (ctx.state().moveTicks == 0) {
            // carried over the obstacle at the height of the lift before coming down onto it,
            // as collisions resolve the vertical axis first
            ctx.holdVertical();
        } else {
            ctx.gravity();
        }
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        return ctx.state().moveTicks >= ctx.settings(this).ticks(TICKS) ? ctx.baseGroundMove() : null;
    }

    @Override
    public ArcadeMove onLand(MoveContext ctx) {
        return this;
    }

    @Override
    @Nullable
    public ArcadeMove onLeaveGround(MoveContext ctx) {
        // the lift leaves the ground for an instant, which is the point
        return ctx.state().moveTicks == 0 ? null : ArcadeMoves.FALL;
    }
}
