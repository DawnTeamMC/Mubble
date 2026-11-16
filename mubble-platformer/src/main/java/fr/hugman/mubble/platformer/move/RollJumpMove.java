package fr.hugman.mubble.platformer.move;

import fr.hugman.mubble.platformer.sim.MoveContext;

/**
 * Jumping out of a roll: a low, long arc keeping all the speed of the roll.
 */
public class RollJumpMove extends ArcMove {
    public RollJumpMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        return ctx.state().move == PlatformerMoves.ROLL && ctx.canJumpFromHere() && ctx.jumpBuffered();
    }
}
