package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.cue.CueEvent;
import fr.hugman.mubble.world.arcade.sim.MoveContext;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

/**
 * Action while crouching on the ground: a low, fast roll, boosted by pressing action again, and
 * gaining speed down any descent. It lasts as long as crouch is held.
 */
public class RollMove extends ArcadeMove {
    /** Speed the roll starts at, unless the player already goes faster. */
    public static final MoveParam SPEED = MoveParam.speed("speed", 0.6D);
    /** Speed a boost adds; the base value of {@code mubble:arcade_roll_boost}, which is what the roll reads. */
    public static final MoveParam BOOST = MoveParam.speed("boost", 0.1D);
    /** How many boosts a roll can take. */
    public static final MoveParam MAX_BOOSTS = MoveParam.of("max_boosts", 3.0D, 0.0D, 64.0D, "count");
    /** Ticks between two boosts. */
    public static final MoveParam BOOST_COOLDOWN = MoveParam.ticks("boost_cooldown_ticks", 4.0D);
    /** Speed lost every tick on flat ground. */
    public static final MoveParam DECEL = MoveParam.speed("decel", 0.004D);
    /** Speed below which the roll stops into a crouch. */
    public static final MoveParam MIN_SPEED = MoveParam.speed("min_speed", 0.15D);
    /** How fast the stick steers the roll, in degrees per tick. */
    public static final MoveParam TURN_SPEED = MoveParam.degrees("turn_speed", 8.0D);
    /** Speed gained per block of step-down, see {@link MoveContext#hugGround}. */
    public static final MoveParam SLOPE_GAIN = MoveParam.factor("slope_gain", 0.12D);
    /** The highest step a roll gets onto without stopping, in blocks: a roll goes up a hill of full blocks. */
    public static final MoveParam CLIMB_HEIGHT = MoveParam.blocks("climb_height", 1.0D);

    public RollMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        return ctx.state().grounded && ctx.crouchHeld() && ctx.actionBuffered();
    }

    @Override
    public void enter(MoveContext ctx) {
        var state = ctx.state();
        ctx.consumeAction();
        float yaw = ctx.hasStick() ? ctx.stickYaw() : (ctx.horizontalSpeed() > 0.05D ? ctx.velocityYaw() : state.facing);
        double speed = ctx.param(this, SPEED);
        if (ctx.previousMove() == ArcadeMoves.GROUND_POUND_LAND) {
            // the ground pound roll
            speed = Math.max(speed, ctx.param(ArcadeMoves.GROUND_POUND_LAND, GroundPoundLandMove.ROLL_SPEED));
        }
        ctx.setHorizontal(yaw, Math.max(ctx.horizontalSpeed(), speed));
        ctx.faceYaw(yaw);
        state.rollBoosts = 0;
        state.rollBoostCooldown = ctx.settings(this).ticks(BOOST_COOLDOWN);
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        double speed = ctx.horizontalSpeed();
        if (ctx.actionBuffered() && state.rollBoostCooldown == 0 && state.rollBoosts < ctx.settings(this).ticks(MAX_BOOSTS)) {
            ctx.consumeAction();
            speed += ctx.tuning().rollBoost();
            state.rollBoosts++;
            state.rollBoostCooldown = ctx.settings(this).ticks(BOOST_COOLDOWN);
            ctx.emit(CueEvent.BOOST, speed);
        }
        float yaw = ctx.velocityYaw();
        if (ctx.hasStick()) {
            yaw = Mth.approachDegrees(yaw, ctx.stickYaw(), (float) ctx.param(this, TURN_SPEED));
        }
        if (!ctx.onMomentumBlock()) {
            speed = Math.max(0.0D, speed - ctx.param(this, DECEL) * ctx.grip());
        }
        ctx.setHorizontal(yaw, speed);
        ctx.faceYaw(yaw);
        double lift = state.grounded ? ctx.liftOver(ctx.param(this, CLIMB_HEIGHT)) : 0.0D;
        if (lift > 0.0D) {
            // up and over the step, then settled onto it, so that the roll stays on the ground
            ctx.nudge(state.vx, lift, state.vz);
            ctx.setDisplacement(0.0D, -0.002D, 0.0D);
            state.vy = 0.0D;
        } else {
            ctx.gravity();
            ctx.hugGround(ctx.param(this, SLOPE_GAIN));
        }
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        if (!ctx.crouchHeld()) {
            return ctx.allowed(ArcadeMoves.RUN) ? ArcadeMoves.RUN : ArcadeMoves.WALK;
        }
        if (ctx.horizontalSpeed() < ctx.param(this, MIN_SPEED)) {
            return ctx.baseGroundMove();
        }
        return null;
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return next == ArcadeMoves.ROLL_JUMP;
    }

    @Override
    public Pose pose(MoveContext ctx) {
        return Pose.SWIMMING;
    }
}
