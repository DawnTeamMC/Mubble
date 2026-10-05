package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.MoveContext;

/**
 * Jumping out of a skid: a high somersault towards the new direction.
 */
public class SideSomersaultMove extends ArcMove {
    /** Speed the somersault carries the player towards the new direction. */
    public static final MoveParam SIDE_SPEED = MoveParam.speed("side_speed", 0.15D);

    public SideSomersaultMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        return ctx.state().move == ArcadeMoves.SKID && ctx.jumpBuffered();
    }

    @Override
    protected void launchHorizontally(MoveContext ctx) {
        // the skid remembers where the player turned to
        float yaw = SkidMove.targetYaw(ctx);
        ctx.setHorizontal(yaw, ctx.param(this, SIDE_SPEED));
        ctx.faceYaw(yaw);
    }
}
