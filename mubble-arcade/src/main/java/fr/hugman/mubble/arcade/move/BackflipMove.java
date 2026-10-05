package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.MoveContext;

/**
 * Crouching and jumping while standing: very high, drifting slightly backwards.
 */
public class BackflipMove extends ArcMove {
    /** Speed of the backward drift. */
    public static final MoveParam BACK_SPEED = MoveParam.speed("back_speed", 0.08D);
    /** Share of the run speed above which crouching and jumping is a long jump rather than a backflip. */
    public static final MoveParam MAX_SPEED = MoveParam.factor("max_speed", 0.3D);

    public BackflipMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        return ctx.state().grounded
                && ctx.crouchHeld()
                && ctx.jumpBuffered()
                && ctx.horizontalSpeed() <= ctx.param(this, MAX_SPEED) * ctx.tuning().effectiveRunSpeed();
    }

    @Override
    protected void launchHorizontally(MoveContext ctx) {
        float facing = ctx.state().facing;
        double speed = ctx.param(this, BACK_SPEED);
        ctx.setVelocity(-MoveContext.directionX(facing) * speed, ctx.state().vy, -MoveContext.directionZ(facing) * speed);
    }
}
