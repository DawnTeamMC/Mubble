package fr.hugman.mubble.world.arcade.sim;

import fr.hugman.mubble.tags.MubbleBlockTags;
import fr.hugman.mubble.world.arcade.cue.CueEvent;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import fr.hugman.mubble.world.arcade.move.ArcadeMoves;
import net.minecraft.world.phys.Vec3;

/**
 * The movement step shared by both sides.
 * <p>
 * The client runs it to predict its own player, the server runs it to check what the client
 * claims, and the replayer runs it to make sure both agree. Nothing in here knows which side it runs
 * on: given the same state, input frame, tuning and world, it always ends in the same state.
 */
public final class ArcadeSimulation {
    /** Ticks a stick spin has to come full circle in to count as one. */
    private static final int SPIN_GESTURE_TICKS = 16;
    /** Displacement below which an axis counts as not moving at all, as far as collisions go. */
    private static final double COLLISION_EPSILON = 1.0E-5D;

    private ArcadeSimulation() {
    }

    /** Plans, moves through {@code body}, and settles one tick. */
    public static MoveContext step(ArcadeState state, ArcadeInputFrame input, ArcadeTuning tuning, ArcadeWorld world, ArcadeBody body) {
        var ctx = plan(state, input, tuning, world, body.position());
        var result = body.move(ctx.pose(), ctx.first(), ctx.second());
        settle(ctx, result);
        return ctx;
    }

    /**
     * Decides the move of the tick and the displacement it asks for.
     */
    public static MoveContext plan(ArcadeState state, ArcadeInputFrame input, ArcadeTuning tuning, ArcadeWorld world, Vec3 position) {
        state.events.clear();
        state.negateFallDamage = false;
        var ctx = new MoveContext(state, input, tuning, world, position);
        ctx.beginTick(SPIN_GESTURE_TICKS);

        // a move taken away mid-way falls back to the plain state of where the player is
        if (!ctx.allowed(state.move)) {
            ctx.switchTo(state.move.isAirborne() || !state.grounded ? ArcadeMoves.FALL : ctx.baseGroundMove());
        }

        var exit = state.move.exit(ctx);
        if (exit != null && exit != state.move) {
            ctx.switchTo(exit);
        }

        for (ArcadeMove candidate : ArcadeMoves.entryOrder()) {
            if (candidate == state.move || !ctx.allowed(candidate)) {
                continue;
            }
            if (!state.move.allowsInterruption(candidate, ctx) || !candidate.canEnter(ctx)) {
                continue;
            }
            ctx.switchTo(candidate);
            break;
        }

        state.move.tick(ctx);
        ctx.finishPlan();
        ctx.endPlan();
        return ctx;
    }

    /**
     * Takes in what the world did with the displacement: collisions, landings, leaving the ground.
     */
    public static void settle(MoveContext ctx, MoveResult result) {
        ctx.setResult(result);
        var state = ctx.state();
        var planned = ctx.total();
        boolean wasGrounded = state.grounded;
        double landingSpeed = Math.max(0.0D, -planned.y);

        if (result.horizontalCollision()) {
            if (Math.abs(planned.x - result.dx()) > COLLISION_EPSILON) {
                state.vx = 0.0D;
            }
            if (Math.abs(planned.z - result.dz()) > COLLISION_EPSILON) {
                state.vz = 0.0D;
            }
        }
        if (result.verticalCollision() && planned.y > 0.0D && result.dy() < planned.y - COLLISION_EPSILON) {
            state.move.onBonk(ctx);
        }

        state.grounded = result.onGround();
        state.pose = ctx.pose();
        if (state.grounded) {
            state.vy = Math.max(state.vy, 0.0D);
        }

        if (!wasGrounded && state.grounded) {
            land(ctx, landingSpeed);
        } else if (wasGrounded && !state.grounded) {
            if (state.move.kind() == ArcadeMove.Kind.GROUND) {
                state.coyote = ctx.tuning().coyoteTicks();
            }
            var next = state.move.onLeaveGround(ctx);
            if (next != null && next != state.move) {
                ctx.switchTo(next);
            }
        }

        if (state.grounded && wasGrounded) {
            state.recordSlope(result.dy(), result.horizontalDistance(), ctx.physics().slope().windowTicks());
        } else if (!state.grounded) {
            state.airTicks++;
        }

        // the cue of the ticks spent in a move, such as the scrape of a wall slide
        ctx.settings(state.move).cues().get(CueEvent.TICK).ifPresent(cue -> {
            if (state.moveTicks % cue.interval() == 0) {
                ctx.emit(CueEvent.TICK, state.horizontalSpeed());
            }
        });
        state.moveTicks++;
    }

    private static void land(MoveContext ctx, double landingSpeed) {
        var state = ctx.state();
        var landedFrom = state.move;
        state.lastLandingSpeed = landingSpeed;
        state.airTicks = 0;
        state.divedThisAir = false;
        state.spunThisAir = false;
        state.coyote = 0;
        state.arcGravity = 0.0D;
        state.clearSlope();

        // the jump chain only goes on from the jump before it
        if (landedFrom == ArcadeMoves.JUMP) {
            state.chainIndex = 1;
            state.chainWindow = ctx.grace().chainWindowTicks();
        } else if (landedFrom == ArcadeMoves.DOUBLE_JUMP) {
            state.chainIndex = 2;
            state.chainWindow = ctx.grace().chainWindowTicks();
        } else {
            state.chainIndex = 0;
            state.chainWindow = 0;
        }

        if (landedFrom.negatesFallDamage(ctx)) {
            state.negateFallDamage = true;
        }

        // a bounce block sends the player back up, unless they crouch on it like on a slime block
        var bounce = ctx.physics().bounce();
        var below = ctx.world().supportingPos(ctx.start().add(ctx.result().dx(), ctx.result().dy(), ctx.result().dz()));
        if (!state.crouchHeld && landingSpeed >= bounce.minFallSpeed() && ctx.world().is(below, MubbleBlockTags.BOUNCE)) {
            boolean pound = landedFrom == ArcadeMoves.GROUND_POUND;
            state.negateFallDamage = true;
            state.chainIndex = 0;
            state.chainWindow = 0;
            ctx.switchTo(ArcadeMoves.FALL);
            ctx.launch(pound ? bounce.groundPoundHeight() : bounce.height(), bounce.ticksToApex(), false);
            state.grounded = false;
            ctx.emit(CueEvent.BOOST, landingSpeed);
            return;
        }

        var next = landedFrom.onLand(ctx);
        if (next != null && next != state.move) {
            ctx.switchTo(next);
        }
        ctx.emit(CueEvent.LAND, landingSpeed);
    }
}
