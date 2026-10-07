package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.MoveContext;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

/**
 * The dive of the water: ground pound, or crouch, and action while swimming dash forward, body flat,
 * much faster than a stroke, and the water slows the dash down until the player swims again. A dive
 * that plunges into water carries on as one.
 */
public class SwimDashMove extends ArcadeMove {
    /** Speed the dash starts at, unless the player already goes faster. */
    public static final MoveParam SPEED = MoveParam.speed("speed", 0.45D);
    /** How long the dash lasts. */
    public static final MoveParam TICKS = MoveParam.ticks("ticks", 12.0D);
    /** Share of its speed the dash keeps every tick. */
    public static final MoveParam DRAG = MoveParam.factor("drag", 0.92D);
    /** How fast the stick steers the dash, in degrees per tick. */
    public static final MoveParam TURN_SPEED = MoveParam.degrees("turn_speed", 4.0D);

    public SwimDashMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        if (!ctx.inDeepWater() || !ctx.actionBuffered()) {
            return false;
        }
        return state.move == ArcadeMoves.GROUND_POUND || state.move.kind() == Kind.WATER && ctx.crouchHeld();
    }

    @Override
    public void enter(MoveContext ctx) {
        var state = ctx.state();
        ctx.consumeAction();
        float yaw = ctx.hasStick() ? ctx.stickYaw() : (ctx.previousMove() == ArcadeMoves.DIVE ? ctx.velocityYaw() : state.facing);
        ctx.setHorizontal(yaw, Math.max(ctx.horizontalSpeed(), ctx.param(this, SPEED)));
        ctx.faceYaw(yaw);
        state.vy = 0.0D;
        state.arcGravity = 0.0D;
        state.jumpCut = true;
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        float yaw = ctx.velocityYaw();
        if (ctx.hasStick()) {
            yaw = Mth.approachDegrees(yaw, ctx.stickYaw(), (float) ctx.param(this, TURN_SPEED));
        }
        double speed = ctx.horizontalSpeed();
        ctx.setHorizontal(yaw, speed);
        ctx.faceYaw(yaw);
        ctx.setDisplacement(state.vx, state.vy, state.vz);
        double drag = ctx.param(this, DRAG);
        state.vx *= drag;
        state.vz *= drag;
        state.vy *= drag;
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        return ctx.state().moveTicks >= ctx.settings(this).ticks(TICKS) ? ArcadeMoves.SWIM : null;
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return next == ArcadeMoves.LEDGE_GRAB;
    }

    @Override
    @Nullable
    public ArcadeMove onLand(MoveContext ctx) {
        return null;
    }

    @Override
    public boolean negatesFallDamage(MoveContext ctx) {
        return true;
    }

    @Override
    public Pose pose(MoveContext ctx) {
        return Pose.SWIMMING;
    }
}
