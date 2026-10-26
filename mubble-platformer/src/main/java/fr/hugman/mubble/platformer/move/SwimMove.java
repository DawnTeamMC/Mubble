package fr.hugman.mubble.platformer.move;

import fr.hugman.mubble.platformer.cue.CueEvent;
import fr.hugman.mubble.platformer.sim.MoveContext;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

/**
 * Being in water deep enough to swim in, on the numbers of vanilla swimming: slow strokes towards the
 * stick, slower drag with sprint held, a slow sink, rising while jump is held and sinking faster while
 * crouch is, depth strider and dolphin's grace included. On top of that, a press of jump strokes
 * upward, or jumps out of the water from the surface; crouch still pounds, slowed down by the water
 * (see {@link GroundPoundMove}); and ground pound, or crouch, and action dash forward (see
 * {@link SwimDashMove}).
 */
public class SwimMove extends PlatformerMove {
    /** Speed the stick adds every tick, as vanilla's. */
    public static final MoveParam ACCEL = MoveParam.speed("accel", LivingEntity.BASE_SWIM_SPEED);
    /** Share of the horizontal speed kept every tick, as vanilla's. */
    public static final MoveParam DRAG = MoveParam.factor("drag", LivingEntity.WATER_DRAG);
    /** The same with sprint held, as vanilla's sprint swimming. */
    public static final MoveParam SPRINT_DRAG = MoveParam.factor("sprint_drag", LivingEntity.SPRINTING_WATER_DRAG);
    /** Upward speed added every tick jump is held, as vanilla's. */
    public static final MoveParam RISE = MoveParam.speed("rise", 0.04D);
    /** Downward speed added every tick crouch is held, as vanilla's. */
    public static final MoveParam SINK = MoveParam.speed("sink", 0.04D);
    /** Upward speed a press of jump strokes up to, under the surface. */
    public static final MoveParam STROKE = MoveParam.speed("stroke", 0.2D);
    /** The deepest the feet may be for jump to jump out of the water rather than stroke: the head is out of it. */
    public static final MoveParam SURFACE_DEPTH = MoveParam.blocks("surface_depth", 1.4D);
    /** How fast the body turns towards the stick, in degrees per tick. */
    public static final MoveParam TURN_SPEED = MoveParam.degrees("turn_speed", 15.0D);

    /** Drag with depth strider at its fullest, as vanilla's, which only writes it out in {@code LivingEntity.travelInWater}. */
    private static final double STRIDER_DRAG = 0.546D;

    public SwimMove(Properties properties) {
        super(properties);
    }

    @Override
    public void enter(MoveContext ctx) {
        var state = ctx.state();
        state.chainIndex = 0;
        state.chainWindow = 0;
        state.arcGravity = 0.0D;
        state.jumpCut = true;
        state.divedThisAir = false;
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        boolean falling = state.vy <= 0.0D;
        if (ctx.jumpBuffered()) {
            // under the surface, jump strokes up; from the surface, it jumps out (see MoveContext#canJumpFromHere)
            ctx.consumeJump();
            state.vy = Math.max(state.vy, ctx.param(this, STROKE));
            ctx.emit(CueEvent.BOOST, state.vy);
        }
        if (ctx.jumpHeld() && ctx.inDeepWater()) {
            state.vy += ctx.param(this, RISE);
        }
        if (ctx.crouchHeld()) {
            state.vy -= ctx.param(this, SINK);
        }

        var tuning = ctx.tuning();
        double drag = ctx.param(this, ctx.sprintHeld() ? SPRINT_DRAG : DRAG);
        double accel = ctx.param(this, ACCEL);
        double strider = tuning.waterEfficiency() * (state.grounded ? 1.0D : 0.5D);
        if (strider > 0.0D) {
            drag += (STRIDER_DRAG - drag) * strider;
            accel += (tuning.movementSpeed() - accel) * strider;
        }
        if (tuning.dolphinsGrace()) {
            drag = LivingEntity.DOLPHINS_GRACE_WATER_DRAG;
        }
        state.vx += ctx.stickX() * accel;
        state.vz += ctx.stickZ() * accel;
        if (ctx.hasStick()) {
            ctx.turnFacing(ctx.stickYaw(), ctx.param(this, TURN_SPEED));
        }

        // as vanilla: the player moves with the speed of the tick, then the water slows it down
        ctx.setDisplacement(state.vx, state.vy, state.vz);
        state.vx *= drag;
        state.vz *= drag;
        // the vertical drag is vanilla's, the water drag of a player who does not sprint, and the slow
        // sink of a player who does nothing in water is vanilla's gravity over 16
        state.vy = state.vy * LivingEntity.WATER_DRAG - tuning.effectiveGravity(falling) / 16.0D;
    }

    /** In water, the moves that make sense: jumping out, pounding, dashing, grabbing a ledge to climb out. */
    @Override
    public boolean allowsInterruption(PlatformerMove next, MoveContext ctx) {
        return next == PlatformerMoves.JUMP || next == PlatformerMoves.GROUND_POUND || next == PlatformerMoves.SWIM_DASH || next == PlatformerMoves.LEDGE_GRAB;
    }

    /** Touching the bottom is not a landing: the player swims on. */
    @Override
    @Nullable
    public PlatformerMove onLand(MoveContext ctx) {
        return null;
    }

    @Override
    public boolean negatesFallDamage(MoveContext ctx) {
        return true;
    }
}
