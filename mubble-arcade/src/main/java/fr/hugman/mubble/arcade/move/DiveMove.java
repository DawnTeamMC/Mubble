package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.MoveContext;
import net.minecraft.world.entity.Pose;

/**
 * Action during a ground pound, as in Super Mario Odyssey: instead of dropping, a forward lunge, body
 * flat, once per airtime. The landing rolls out of it, see {@link RolloutMove}.
 */
public class DiveMove extends ArcadeMove {
    /** Speed the dive lunges at, unless the player already goes faster. */
    public static final MoveParam SPEED = MoveParam.speed("speed", 0.5D);
    /** Upward speed the lunge starts with. */
    public static final MoveParam LIFT = MoveParam.speed("lift", 0.25D);
    /** Scale of the air control during the dive. */
    public static final MoveParam AIR_CONTROL = MoveParam.factor("air_control", 0.3D);

    public DiveMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        return !state.grounded && state.move == ArcadeMoves.GROUND_POUND && ctx.actionBuffered() && !state.divedThisAir && !ctx.inDeepWater();
    }

    @Override
    public void enter(MoveContext ctx) {
        var state = ctx.state();
        ctx.consumeAction();
        float yaw = ctx.hasStick() ? ctx.stickYaw() : state.facing;
        ctx.setHorizontal(yaw, Math.max(ctx.horizontalSpeed(), ctx.param(this, SPEED)));
        ctx.faceYaw(yaw);
        state.vy = Math.max(state.vy, ctx.param(this, LIFT));
        state.arcGravity = 0.0D;
        state.jumpCut = true;
        state.divedThisAir = true;
    }

    @Override
    public void tick(MoveContext ctx) {
        ctx.airMotion(ctx.param(this, AIR_CONTROL));
        ctx.gravity();
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return false;
    }

    @Override
    public ArcadeMove onLand(MoveContext ctx) {
        return ctx.allowed(ArcadeMoves.ROLLOUT) ? ArcadeMoves.ROLLOUT : ctx.landingMove();
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
