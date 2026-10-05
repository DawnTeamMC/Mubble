package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.MoveContext;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Hanging from an edge: caught on the way down, as long as the player faces it. Jump climbs up,
 * crouch lets go, and the stick shimmies along the edge.
 * <p>
 * The edge pulls a player within reach in (ledge magnetism), but it never catches anyone on the way
 * up: a grab must never feel like it stole a jump.
 */
public class LedgeGrabMove extends ArcadeMove {
    /** How far below the edge the top of the hitbox hangs. */
    public static final MoveParam HANG_DEPTH = MoveParam.blocks("hang_depth", 0.3D);
    /** How far below the hanging height an edge still gets caught. */
    public static final MoveParam GRAB_BAND = MoveParam.blocks("grab_band", 0.6D);
    /** How far above the feet the edge has to be, so that steps are walked onto rather than grabbed. */
    public static final MoveParam MIN_HEIGHT = MoveParam.blocks("min_height", 1.0D);
    /** Speed of the shimmy along the edge. */
    public static final MoveParam SHIMMY_SPEED = MoveParam.speed("shimmy_speed", 0.1D);

    public LedgeGrabMove(Properties properties) {
        super(properties);
    }

    public static Direction face(MoveContext ctx) {
        return Direction.from2DDataValue(Math.max(0, ctx.state().ledgeFace));
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        if (state.grounded || state.vy > 0.0D || state.ledgeRegrab > 0) {
            return false;
        }
        double fx = MoveContext.directionX(state.facing);
        double fz = MoveContext.directionZ(state.facing);
        var direction = MoveContext.horizontalDirection(fx, fz);
        if (fx * direction.getStepX() + fz * direction.getStepZ() < ctx.grace().ledgeFacingDot()) {
            return false;
        }
        if (ctx.hasStick() && ctx.stickAlong(direction.getStepX(), direction.getStepZ()) < 0.0D) {
            // pulling away from the wall is a way of saying no
            return false;
        }
        var ledge = ctx.findLedge(direction, ctx.param(this, HANG_DEPTH), ctx.param(this, GRAB_BAND), ctx.param(this, MIN_HEIGHT));
        if (ledge == null) {
            return false;
        }
        state.ledgeFace = direction.get2DDataValue();
        state.ledgeY = ledge.top();
        state.anchorX = ledge.hangPosition().x;
        state.anchorY = ledge.hangPosition().y;
        state.anchorZ = ledge.hangPosition().z;
        return true;
    }

    @Override
    public void enter(MoveContext ctx) {
        ctx.setVelocity(0.0D, 0.0D, 0.0D);
        ctx.state().jumpCut = true;
        ctx.faceYaw(face(ctx).toYRot());
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        state.vx = 0.0D;
        state.vy = 0.0D;
        state.vz = 0.0D;
        var start = ctx.start();
        if (state.moveTicks == 0) {
            // pulled in onto the edge
            ctx.setDisplacement(state.anchorX - start.x, state.anchorY - start.y, state.anchorZ - start.z);
            return;
        }
        var direction = face(ctx);
        var along = direction.getClockWise();
        double push = ctx.stickAlong(along.getStepX(), along.getStepZ()) * ctx.stickMagnitude();
        if (Math.abs(push) > 0.3D) {
            double step = Math.signum(push) * ctx.param(this, SHIMMY_SPEED);
            var target = start.add(along.getStepX() * step, 0.0D, along.getStepZ() * step);
            var ledge = this.ledgeAt(ctx, target, direction);
            if (ledge != null) {
                state.ledgeY = ledge.top();
                ctx.setDisplacement(ledge.hangPosition().x - start.x, ledge.hangPosition().y - start.y, ledge.hangPosition().z - start.z);
                return;
            }
        }
        ctx.setDisplacement(0.0D, 0.0D, 0.0D);
    }

    private MoveContext.@Nullable Ledge ledgeAt(MoveContext ctx, Vec3 position, Direction direction) {
        return ctx.findLedge(position, direction, ctx.param(this, HANG_DEPTH), ctx.param(this, GRAB_BAND), ctx.param(this, MIN_HEIGHT), ctx.grace().ledgeMagnetism(), 0.0D);
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        var state = ctx.state();
        if (state.moveTicks == 0) {
            return null;
        }
        if (ctx.jumpBuffered() && ctx.allowed(ArcadeMoves.LEDGE_CLIMB)) {
            ctx.consumeJump();
            return ArcadeMoves.LEDGE_CLIMB;
        }
        if (ctx.crouchPressed() || this.ledgeAt(ctx, ctx.start(), face(ctx)) == null) {
            // letting go is all the press of crouch does: it is no ground pound
            ctx.consumeCrouch();
            state.ledgeRegrab = ctx.grace().ledgeRegrabTicks();
            return ArcadeMoves.FALL;
        }
        return null;
    }
}
