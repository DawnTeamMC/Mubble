package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;
import net.minecraft.world.entity.Pose;

/**
 * Crouching and jumping while running: low, far and fast, body flat.
 */
public class LongJumpMove extends ArcMove {
    /** Speed the long jump launches at, unless the player already goes faster. */
    public static final MoveParam SPEED = MoveParam.speed("speed", 0.55D);
    /** Share of the run speed the player needs to move at. */
    public static final MoveParam MIN_SPEED = MoveParam.factor("min_speed", 0.5D);

    public LongJumpMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        return ctx.canJumpFromHere()
                && ctx.crouchHeld()
                && ctx.jumpBuffered()
                && ctx.horizontalSpeed() >= ctx.param(this, MIN_SPEED) * ctx.tuning().effectiveRunSpeed();
    }

    @Override
    protected void launchHorizontally(MoveContext ctx) {
        float yaw = ctx.hasStick() ? ctx.stickYaw() : ctx.velocityYaw();
        ctx.setHorizontal(yaw, Math.max(ctx.horizontalSpeed(), ctx.param(this, SPEED)));
        ctx.faceYaw(yaw);
    }

    @Override
    public Pose pose(MoveContext ctx) {
        return Pose.SWIMMING;
    }
}
