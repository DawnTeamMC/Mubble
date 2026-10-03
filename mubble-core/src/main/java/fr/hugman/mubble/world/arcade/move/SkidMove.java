package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;
import org.jspecify.annotations.Nullable;

/**
 * Reversing the stick at speed: a short brake, after which the player runs off the other way. Jumping
 * during it is a side somersault.
 */
public class SkidMove extends ArcadeMove {
    /** Share of the run speed the player needs to move at for a reversal to skid. */
    public static final MoveParam MIN_SPEED = MoveParam.factor("min_speed", 0.6D);
    /** How opposed the stick has to be to the velocity, as the cosine of the angle between them. */
    public static final MoveParam REVERSE_DOT = MoveParam.of("reverse_dot", -0.5D, -1.0D, 1.0D, "cosine");
    /** How long the skid lasts. */
    public static final MoveParam TICKS = MoveParam.ticks("ticks", 6.0D);
    /** Share of its speed the skid loses every tick. */
    public static final MoveParam BRAKE = MoveParam.factor("brake", 0.3D);
    /** Speed the player sets off at in the new direction. */
    public static final MoveParam EXIT_SPEED = MoveParam.speed("exit_speed", 0.1D);

    public SkidMove(Properties properties) {
        super(properties);
    }

    /** Where the player turned to, kept in the anchor of the state for the somersault. */
    public static float targetYaw(MoveContext ctx) {
        return (float) ctx.state().anchorX;
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        if (!state.grounded || state.move != ArcadeMoves.RUN || ctx.stickMagnitude() < 0.5D) {
            return false;
        }
        double speed = ctx.horizontalSpeed();
        return speed >= ctx.param(this, MIN_SPEED) * ctx.tuning().effectiveRunSpeed()
                && ctx.stickAlong(state.vx, state.vz) <= ctx.param(this, REVERSE_DOT);
    }

    @Override
    public void enter(MoveContext ctx) {
        ctx.state().anchorX = ctx.stickYaw();
    }

    @Override
    public void tick(MoveContext ctx) {
        if (ctx.hasStick()) {
            ctx.state().anchorX = ctx.stickYaw();
        }
        double speed = ctx.horizontalSpeed() * (1.0D - ctx.param(this, BRAKE) * ctx.grip());
        ctx.setHorizontal(ctx.velocityYaw(), speed);
        ctx.gravity();
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        if (ctx.state().moveTicks >= ctx.settings(this).ticks(TICKS)) {
            float yaw = targetYaw(ctx);
            ctx.setHorizontal(yaw, ctx.param(this, EXIT_SPEED));
            ctx.faceYaw(yaw);
            return ctx.baseGroundMove();
        }
        return null;
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return next == ArcadeMoves.SIDE_SOMERSAULT || (next == ArcadeMoves.JUMP && !ctx.allowed(ArcadeMoves.SIDE_SOMERSAULT));
    }
}
