package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.MoveContext;

/**
 * Crouching in the air: a brief hang, then a straight, fast drop. Its landing takes no fall damage
 * and can be jumped out of very high, see {@link GroundPoundJumpMove}. Action, at any point of it,
 * dives forward instead, see {@link DiveMove}.
 */
public class GroundPoundMove extends ArcadeMove {
    /** How long the player hangs before dropping. */
    public static final MoveParam HANG_TICKS = MoveParam.ticks("hang_ticks", 6.0D);
    /** Speed of the drop; the base value of {@code mubble:arcade_ground_pound_speed}, which is what the drop reads. */
    public static final MoveParam DROP_SPEED = MoveParam.speed("drop_speed", 1.2D);
    /** How high above the ground the player has to be. */
    public static final MoveParam MIN_HEIGHT = MoveParam.blocks("min_height", 1.0D);

    public GroundPoundMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        if (ctx.state().grounded || !ctx.crouchPressed()) {
            return false;
        }
        double minHeight = ctx.param(this, MIN_HEIGHT);
        return ctx.heightAboveGround(minHeight + 0.01D) >= minHeight;
    }

    @Override
    public void enter(MoveContext ctx) {
        ctx.setVelocity(0.0D, 0.0D, 0.0D);
        ctx.state().jumpCut = true;
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        state.vx = 0.0D;
        state.vz = 0.0D;
        if (state.moveTicks < ctx.settings(this).ticks(HANG_TICKS)) {
            ctx.holdVertical();
        } else {
            state.vy = -ctx.tuning().groundPoundSpeed();
            ctx.setVerticalDisplacement(state.vy);
        }
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return next == ArcadeMoves.DIVE;
    }

    @Override
    public ArcadeMove onLand(MoveContext ctx) {
        return ArcadeMoves.GROUND_POUND_LAND;
    }

    @Override
    public boolean negatesFallDamage(MoveContext ctx) {
        return true;
    }
}
