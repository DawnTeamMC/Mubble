package fr.hugman.mubble.platformer.move;

import fr.hugman.mubble.platformer.sim.MoveContext;

/**
 * Being in the air without a move of one's own: walking off a ledge, letting go of one, bouncing.
 */
public class FallMove extends PlatformerMove {
    public FallMove(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(MoveContext ctx) {
        ctx.airMotion(1.0D);
        ctx.gravity();
    }

    @Override
    public boolean allowsInterruption(PlatformerMove next, MoveContext ctx) {
        // within coyote time, the ground is still close enough to jump off it
        if (ctx.state().coyote > 0 && next.kind() != Kind.GROUND) {
            return true;
        }
        return PlatformerMoves.isAerialFollowUp(next);
    }
}
