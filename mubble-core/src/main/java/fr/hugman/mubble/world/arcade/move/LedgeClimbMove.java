package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

/**
 * Pulling up from a ledge: up along the wall first, then forward onto the edge.
 */
public class LedgeClimbMove extends ArcadeMove {
    /** How long the pull-up lasts. */
    public static final MoveParam TICKS = MoveParam.ticks("ticks", 8.0D);
    /** How far past the edge the pull-up ends, in blocks. */
    public static final MoveParam FORWARD = MoveParam.blocks("forward", 0.7D);

    public LedgeClimbMove(Properties properties) {
        super(properties);
    }

    @Override
    public void enter(MoveContext ctx) {
        var state = ctx.state();
        state.anchorX = ctx.start().x;
        state.anchorY = ctx.start().y;
        state.anchorZ = ctx.start().z;
        ctx.setVelocity(0.0D, 0.0D, 0.0D);
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        int total = Math.max(2, ctx.settings(this).ticks(TICKS));
        int rise = total / 2;
        int k = state.moveTicks;
        var start = ctx.start();
        if (k < rise) {
            double y = state.anchorY + (state.ledgeY + 0.001D - state.anchorY) * (k + 1) / rise;
            ctx.setDisplacement(0.0D, y - start.y, 0.0D);
        } else {
            var direction = LedgeGrabMove.face(ctx);
            double step = ctx.param(this, FORWARD) / (total - rise);
            // over the edge first, then down onto it: collisions resolve the vertical axis first
            ctx.setDisplacement(0.0D, -0.002D, 0.0D);
            ctx.nudge(direction.getStepX() * step, 0.0D, direction.getStepZ() * step);
        }
        state.vx = 0.0D;
        state.vy = 0.0D;
        state.vz = 0.0D;
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        return ctx.state().moveTicks >= Math.max(2, ctx.settings(this).ticks(TICKS)) ? ctx.baseGroundMove() : null;
    }

    @Override
    public ArcadeMove onLand(MoveContext ctx) {
        // reaching the top of the edge is part of the climb, not the end of it
        return this;
    }

    @Override
    public Pose pose(MoveContext ctx) {
        return Pose.CROUCHING;
    }
}
