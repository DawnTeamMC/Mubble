package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

/**
 * Crouching on a descent while running: a slide that picks up speed as long as the ground goes down,
 * steered by the stick.
 */
public class SlideMove extends ArcadeMove {
    /** Share of the run speed the player needs to move at. */
    public static final MoveParam MIN_SPEED = MoveParam.factor("min_speed", 0.6D);
    /** The gentlest descent a slide starts on, as height lost per block travelled. */
    public static final MoveParam MIN_SLOPE = MoveParam.factor("min_slope", 0.25D);
    /** Speed gained every tick for every unit of slope. */
    public static final MoveParam GAIN = MoveParam.speed("gain", 0.08D);
    /** Speed lost every tick on flat ground. */
    public static final MoveParam FLAT_DECEL = MoveParam.speed("flat_decel", 0.02D);
    /** Speed below which the slide stops. */
    public static final MoveParam EXIT_SPEED = MoveParam.speed("exit_speed", 0.15D);
    /** How fast the stick steers the slide, in degrees per tick. */
    public static final MoveParam TURN_SPEED = MoveParam.degrees("turn_speed", 6.0D);
    /** Speed gained per block of step-down, see {@link MoveContext#hugGround}. */
    public static final MoveParam SLOPE_GAIN = MoveParam.factor("slope_gain", 0.12D);

    public SlideMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        return state.grounded
                && (state.move == ArcadeMoves.RUN || state.move == ArcadeMoves.LAND)
                && ctx.crouchPressed()
                && ctx.horizontalSpeed() >= ctx.param(this, MIN_SPEED) * ctx.tuning().effectiveRunSpeed()
                && state.slope() <= -ctx.param(this, MIN_SLOPE);
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        double slope = state.slope();
        double speed = ctx.horizontalSpeed();
        if (slope < 0.0D) {
            speed += ctx.param(this, GAIN) * -slope;
        } else if (!ctx.onMomentumBlock()) {
            speed = Math.max(0.0D, speed - ctx.param(this, FLAT_DECEL) * ctx.grip());
        }
        float yaw = ctx.velocityYaw();
        if (ctx.hasStick()) {
            yaw = Mth.approachDegrees(yaw, ctx.stickYaw(), (float) ctx.param(this, TURN_SPEED));
        }
        ctx.setHorizontal(yaw, speed);
        ctx.faceYaw(yaw);
        ctx.gravity();
        ctx.hugGround(ctx.param(this, SLOPE_GAIN));
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        if (!ctx.crouchHeld() || ctx.horizontalSpeed() < ctx.param(this, EXIT_SPEED)) {
            return ctx.baseGroundMove();
        }
        return null;
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return next.isAirborne() || next == ArcadeMoves.ROLL;
    }

    @Override
    public Pose pose(MoveContext ctx) {
        return Pose.CROUCHING;
    }
}
