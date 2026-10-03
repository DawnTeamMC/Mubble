package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;

/**
 * The jump, and the double and triple jumps it chains into.
 * <p>
 * A jump landed and followed by another within the chain window, while moving, becomes a double
 * jump; a double jump followed the same way becomes a triple jump, as long as the player runs fast
 * enough. Each one goes higher than the one before.
 */
public class JumpMove extends ArcMove {
    /** Share of the run speed the player needs to move at for the jump to chain. */
    public static final MoveParam MIN_SPEED = MoveParam.factor("min_speed", 0.0D);

    /** How many jumps have to have been chained before this one: 0 for the jump, 1 for the double, 2 for the triple. */
    private final int chainIndex;

    public JumpMove(Properties properties, int chainIndex) {
        super(properties);
        this.chainIndex = chainIndex;
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        if (!ctx.canJumpFromHere() || !ctx.jumpBuffered()) {
            return false;
        }
        if (this.chainIndex == 0) {
            return true;
        }
        var state = ctx.state();
        return state.chainIndex == this.chainIndex
                && state.chainWindow > 0
                && ctx.horizontalSpeed() >= ctx.param(this, MIN_SPEED) * ctx.tuning().effectiveRunSpeed();
    }
}
