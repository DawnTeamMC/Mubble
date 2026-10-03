package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;

/**
 * Kicking off a wall: up and away from it.
 * <p>
 * It is accepted while sliding down the wall, and for a few ticks after leaving it, see
 * {@link fr.hugman.mubble.world.arcade.ArcadeGrace#wallJumpLeniencyTicks()}.
 */
public class WallJumpMove extends ArcMove {
    /** Speed the kick pushes the player away from the wall at. */
    public static final MoveParam PUSH = MoveParam.speed("push", 0.35D);
    /** Ticks during which the stick cannot steer back, so that the kick is not undone right away. */
    public static final MoveParam LOCK_TICKS = MoveParam.ticks("lock_ticks", 6.0D);

    public WallJumpMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        return !state.grounded && ctx.jumpBuffered() && (state.move == ArcadeMoves.WALL_SLIDE || state.wallLeniency > 0);
    }

    @Override
    protected void launchHorizontally(MoveContext ctx) {
        var state = ctx.state();
        double push = ctx.param(this, PUSH);
        state.vx = state.wallNormalX * push;
        state.vz = state.wallNormalZ * push;
        state.wallLeniency = 0;
        state.controlLock = ctx.settings(this).ticks(LOCK_TICKS);
        ctx.faceYaw(MoveContext.yawOf(state.wallNormalX, state.wallNormalZ));
    }

    @Override
    public void tick(MoveContext ctx) {
        ctx.airMotion(ctx.state().controlLock > 0 ? 0.0D : ctx.param(this, AIR_CONTROL));
        ctx.gravity();
    }
}
