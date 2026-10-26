package fr.hugman.mubble.platformer.sim;

import fr.hugman.mubble.platformer.cue.CueEvent;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.move.ClimbMove;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

/**
 * The movement step shared by both sides.
 * <p>
 * The client runs it to predict its own player, the server runs it to check what the client
 * claims, and the replayer runs it to make sure both agree. Nothing in here knows which side it runs
 * on: given the same state, input frame, tuning and world, it always ends in the same state.
 */
public final class PlatformerSimulation {
    /** Displacement below which an axis counts as not moving at all, as far as collisions go. */
    private static final double COLLISION_EPSILON = 1.0E-5D;
    /** Upward speed from which a push launches the player rather than nudging them. */
    private static final double LAUNCH_THRESHOLD = 1.0E-3D;

    private PlatformerSimulation() {
    }

    /**
     * Takes in a velocity set from outside the simulation, such as knockback or a launcher. A push
     * upwards throws the player off whatever move held them: off the ground, off a wall, out of the arc
     * of a jump, into a plain fall under base gravity.
     */
    public static void applyImpulse(PlatformerState state, Vec3 impulse) {
        state.vx = impulse.x;
        state.vy = impulse.y;
        state.vz = impulse.z;
        if (impulse.y > LAUNCH_THRESHOLD && state.move.kind() != PlatformerMove.Kind.WATER) {
            state.grounded = false;
            state.coyote = 0;
            state.arcGravity = 0.0D;
            state.jumpCut = true;
            if (state.move != PlatformerMoves.FALL) {
                state.move = PlatformerMoves.FALL;
                state.moveTicks = 0;
                state.moveSeq++;
            }
        }
    }

    /** Plans, moves through {@code body}, and settles one tick. */
    public static MoveContext step(PlatformerState state, PlatformerInputFrame input, PlatformerTuning tuning, PlatformerWorld world, PlatformerBody body) {
        var ctx = plan(state, input, tuning, world, body.position());
        var result = body.move(ctx.pose(), ctx.first(), ctx.second());
        settle(ctx, result);
        return ctx;
    }

    /**
     * Decides the move of the tick and the displacement it asks for.
     */
    public static MoveContext plan(PlatformerState state, PlatformerInputFrame input, PlatformerTuning tuning, PlatformerWorld world, Vec3 position) {
        state.events.clear();
        state.negateFallDamage = false;
        var ctx = new MoveContext(state, input, tuning, world, position);
        ctx.beginTick();

        // a move taken away mid-way falls back to the plain state of where the player is
        if (!ctx.allowed(state.move)) {
            ctx.switchTo(state.move.isAirborne() || !state.grounded ? PlatformerMoves.FALL : ctx.baseGroundMove());
        }

        climb(ctx);
        water(ctx);

        var exit = state.move.exit(ctx);
        if (exit != null && exit != state.move) {
            ctx.switchTo(exit);
        }

        for (PlatformerMove candidate : PlatformerMoves.entryOrder()) {
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
        // known before the body moves, since a landing hurts the player during that move
        if (!state.grounded && (state.move.negatesFallDamage(ctx) || ctx.wouldRollOnLanding())) {
            state.negateFallDamage = true;
        }
        return ctx;
    }

    /**
     * Something to climb catches the player, whatever they were doing, in water too: only a ledge or
     * a wall they hold already keeps them. The climb itself lets go, see {@link ClimbMove#exit}.
     */
    private static void climb(MoveContext ctx) {
        var move = ctx.state().move;
        if (move != PlatformerMoves.CLIMB && move.kind() != PlatformerMove.Kind.ATTACHED && ctx.onClimbable()) {
            ctx.switchTo(PlatformerMoves.CLIMB);
        }
    }

    /**
     * Water deep enough to swim in takes the player over, and lets go of them once out of it, or once
     * they stand in water too shallow to swim. A pound carries on down, slowed by the water, a dive
     * carries on as a dash, and a player rising out of the water is left to rise.
     */
    private static void water(MoveContext ctx) {
        var state = ctx.state();
        var move = state.move;
        if (move.kind() == PlatformerMove.Kind.WATER) {
            if (ctx.waterDepth() <= 0.0D) {
                ctx.switchTo(state.grounded ? ctx.baseGroundMove() : PlatformerMoves.FALL);
            } else if (state.grounded && !ctx.inDeepWater()) {
                ctx.switchTo(ctx.baseGroundMove());
            }
            return;
        }
        if (!ctx.inDeepWater() || move.kind() == PlatformerMove.Kind.ATTACHED
                || move == PlatformerMoves.GROUND_POUND || move == PlatformerMoves.GROUND_POUND_LAND
                || move.kind() == PlatformerMove.Kind.AIR && state.vy > 0.0D) {
            return;
        }
        ctx.switchTo(move == PlatformerMoves.DIVE && ctx.allowed(PlatformerMoves.SWIM_DASH) ? PlatformerMoves.SWIM_DASH : PlatformerMoves.SWIM);
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
            if (state.move.kind() == PlatformerMove.Kind.GROUND) {
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
        if (landedFrom == PlatformerMoves.JUMP) {
            state.chainIndex = 1;
            state.chainWindow = ctx.grace().chainWindowTicks();
        } else if (landedFrom == PlatformerMoves.DOUBLE_JUMP) {
            state.chainIndex = 2;
            state.chainWindow = ctx.grace().chainWindowTicks();
        } else {
            state.chainIndex = 0;
            state.chainWindow = 0;
        }

        if (landedFrom.negatesFallDamage(ctx)) {
            state.negateFallDamage = true;
        }

        // a bouncy block sends the player back up, as high as it is bouncy, unless they crouch on it like vanilla's sneak
        var bounce = ctx.physics().bounce();
        var below = ctx.world().supportingPos(ctx.start().add(ctx.result().dx(), ctx.result().dy(), ctx.result().dz()));
        double restitution = bounceRestitution(ctx, below, landingSpeed);
        if (restitution > 0.0D && landingSpeed >= bounce.minFallSpeed()) {
            boolean pound = landedFrom == PlatformerMoves.GROUND_POUND;
            state.negateFallDamage = true;
            state.chainIndex = 0;
            state.chainWindow = 0;
            ctx.switchTo(PlatformerMoves.FALL);
            ctx.launch((pound ? bounce.groundPoundHeight() : bounce.height()) * restitution, bounce.ticksToApex(), false);
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

    /**
     * How bouncy a landing on {@code below} is, the way vanilla restitutes a fall onto a block: the
     * most of the block's bounce restitution (slime, beds...) and of the player's bounciness attribute,
     * none when the block suppresses bounce, when the player holds crouch, vanilla's sneak, or when the
     * fall was too slow to be more than a tick of gravity.
     * <p>
     * The platformer layer only borrows the trigger and the strength: rather than vanilla's mirrored
     * velocity, the bounce reaches the heights of the profile, scaled by the restitution, whatever the
     * fall. Vanilla still restitutes the velocity of the entity as it moves, which is harmless: the step
     * writes its own velocity over it right after.
     */
    private static double bounceRestitution(MoveContext ctx, BlockPos below, double landingSpeed) {
        var block = ctx.world().blockState(below);
        if (ctx.state().crouchHeld || block.is(BlockTags.SUPPRESSES_BOUNCE) || landingSpeed < effectiveGravity(ctx.tuning())) {
            return 0.0D;
        }
        double bounciness = ctx.world().player().getAttributeValue(Attributes.BOUNCINESS);
        return Math.max(bounciness, block.getBlock().getBounceRestitution());
    }

    /** The gravity of a falling player, as vanilla has it: slow falling softens it. */
    private static double effectiveGravity(PlatformerTuning tuning) {
        double gravity = PlatformerTuning.VANILLA_GRAVITY * tuning.gravityScale();
        return tuning.slowFalling() ? Math.min(gravity, 0.01D) : gravity;
    }
}
