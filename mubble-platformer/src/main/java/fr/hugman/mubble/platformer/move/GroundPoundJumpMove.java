package fr.hugman.mubble.platformer.move;

import fr.hugman.mubble.platformer.sim.MoveContext;

/**
 * Jumping right out of a ground pound landing: a very high, straight jump.
 */
public class GroundPoundJumpMove extends ArcMove {
    public GroundPoundJumpMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        return state.move == PlatformerMoves.GROUND_POUND_LAND
                && state.moveTicks <= ctx.settings(PlatformerMoves.GROUND_POUND_LAND).ticks(GroundPoundLandMove.JUMP_WINDOW)
                && ctx.jumpBuffered()
                && !ctx.inDeepWater();
    }

    @Override
    protected void launchHorizontally(MoveContext ctx) {
        ctx.state().vx = 0.0D;
        ctx.state().vz = 0.0D;
    }
}
