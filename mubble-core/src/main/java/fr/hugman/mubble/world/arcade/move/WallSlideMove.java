package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

/**
 * Holding towards a wall while falling: the fall is capped, and jump kicks off the wall. Letting go
 * still leaves a few ticks to kick, see {@link WallJumpMove}.
 */
public class WallSlideMove extends ArcadeMove {
    /** How high above the ground the player has to be. */
    public static final MoveParam MIN_HEIGHT = MoveParam.blocks("min_height", 1.0D);
    /** How squarely the stick has to point at the wall, as the cosine of the largest angle allowed. */
    public static final MoveParam STICK_DOT = MoveParam.of("stick_dot", 0.4D, -1.0D, 1.0D, "cosine");
    /** The fastest slide down the wall; the base value of {@code mubble:arcade_wall_slide_speed}, which is what the slide reads. */
    public static final MoveParam MAX_FALL_SPEED = MoveParam.speed("max_fall_speed", 0.15D);
    /** How far the wall may be from the player. */
    public static final MoveParam REACH = MoveParam.blocks("reach", 0.1D);

    public WallSlideMove(Properties properties) {
        super(properties);
    }

    private static Direction towardsWall(MoveContext ctx) {
        var state = ctx.state();
        return MoveContext.horizontalDirection(-state.wallNormalX, -state.wallNormalZ);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        if (state.grounded || state.vy >= 0.0D || !ctx.hasStick()) {
            return false;
        }
        var direction = MoveContext.horizontalDirection(ctx.stickX(), ctx.stickZ());
        if (ctx.stickAlong(direction.getStepX(), direction.getStepZ()) < ctx.param(this, STICK_DOT)) {
            return false;
        }
        double minHeight = ctx.param(this, MIN_HEIGHT);
        if (ctx.heightAboveGround(minHeight + 0.01D) < minHeight) {
            return false;
        }
        var wall = ctx.findWall(direction, ctx.param(this, REACH));
        if (wall == null) {
            return false;
        }
        state.wallNormalX = wall.normalX();
        state.wallNormalZ = wall.normalZ();
        return true;
    }

    @Override
    public void enter(MoveContext ctx) {
        var state = ctx.state();
        ctx.faceYaw(MoveContext.yawOf(state.wallNormalX, state.wallNormalZ));
        state.jumpCut = true;
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        // a slight push keeps the player against the wall
        double push = ctx.param(this, REACH) * 0.5D;
        state.vx = -state.wallNormalX * push;
        state.vz = -state.wallNormalZ * push;
        ctx.cappedGravity(ctx.tuning().wallSlideSpeed());
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        var state = ctx.state();
        var direction = towardsWall(ctx);
        boolean holding = ctx.stickAlong(direction.getStepX(), direction.getStepZ()) >= ctx.param(this, STICK_DOT);
        if (!holding || ctx.findWall(direction, ctx.param(this, REACH) * 2.0D) == null) {
            state.wallLeniency = ctx.grace().wallJumpLeniencyTicks();
            return ArcadeMoves.FALL;
        }
        return null;
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return next == ArcadeMoves.WALL_JUMP || next == ArcadeMoves.LEDGE_GRAB;
    }
}
