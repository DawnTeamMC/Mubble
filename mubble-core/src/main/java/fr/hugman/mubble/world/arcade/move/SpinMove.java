package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;

/**
 * Spinning the stick around (or pressing the spin key): in the air, a small lift and a slowed fall,
 * once per airtime; on the ground with jump, a spinning jump ending the same way.
 */
public class SpinMove extends ArcadeMove {
    /** The fastest fall while spinning. */
    public static final MoveParam FALL_SPEED = MoveParam.speed("fall_speed", 0.08D);
    /** How long the slowed fall lasts. */
    public static final MoveParam TICKS = MoveParam.ticks("ticks", 14.0D);
    /** Height of the lift of a spin started in the air. */
    public static final MoveParam LIFT_HEIGHT = MoveParam.blocks("lift_height", 0.6D);
    /** Ticks the lift takes to reach that height. */
    public static final MoveParam LIFT_TICKS = MoveParam.of("lift_ticks", 4.0D, 1.0D, 100.0D, "ticks");
    /** Height of the spinning jump. */
    public static final MoveParam JUMP_HEIGHT = MoveParam.blocks("jump_height", 3.0D);
    /** Ticks the spinning jump takes to reach that height. */
    public static final MoveParam JUMP_TICKS = MoveParam.of("jump_ticks", 9.0D, 1.0D, 100.0D, "ticks");
    /** Scale of the air control while spinning. */
    public static final MoveParam AIR_CONTROL = MoveParam.factor("air_control", 1.0D);

    public SpinMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        var state = ctx.state();
        boolean asked = ctx.spinPressed() || ctx.spinGesture();
        if (!asked) {
            return false;
        }
        if (ctx.canJumpFromHere()) {
            return ctx.jumpBuffered() || ctx.spinPressed();
        }
        return !state.spunThisAir;
    }

    @Override
    public void enter(MoveContext ctx) {
        var state = ctx.state();
        boolean fromGround = ctx.canJumpFromHere();
        ctx.consumeSpinGesture();
        state.spunThisAir = true;
        if (fromGround) {
            ctx.launch(ctx.param(this, JUMP_HEIGHT), ctx.param(this, JUMP_TICKS), true);
        } else {
            double vy = state.vy;
            ctx.launch(ctx.param(this, LIFT_HEIGHT), ctx.param(this, LIFT_TICKS), false);
            state.vy = Math.max(vy, state.vy);
        }
    }

    @Override
    public void tick(MoveContext ctx) {
        ctx.airMotion(ctx.param(this, AIR_CONTROL));
        if (ctx.state().moveTicks < ctx.settings(this).ticks(TICKS)) {
            ctx.cappedGravity(ctx.param(this, FALL_SPEED));
        } else {
            ctx.gravity();
        }
    }
}
