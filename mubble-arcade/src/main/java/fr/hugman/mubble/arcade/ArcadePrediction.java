package fr.hugman.mubble.arcade;

import fr.hugman.mubble.arcade.network.ArcadeCorrectionPayload;
import fr.hugman.mubble.arcade.network.ArcadeInputPayload;
import fr.hugman.mubble.arcade.replay.ArcadeReplayer;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import fr.hugman.mubble.arcade.sim.ArcadeSimulation;
import fr.hugman.mubble.arcade.sim.MoveContext;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * The predicting half of the lockstep: the client steps the simulation ahead of the server and tells
 * it what it did, then takes the corrections the server sends back.
 * <p>
 * None of this needs the client classes, so that tests can drive a stand-in client through the very
 * same code as the game does.
 */
public final class ArcadePrediction {
    /** Below this, a difference between the velocity written last tick and the one found now is rounding, not a push. */
    private static final double IMPULSE_EPSILON = 1.0E-6D;

    private ArcadePrediction() {
    }

    /**
     * Steps {@code player} by one tick of {@code frame}, through the collisions of its level.
     *
     * @return the step, with the payload that reports it to the server
     */
    public static Step step(Player player, ArcadeController controller, ArcadeInputFrame frame) {
        if (controller.needsReset()) {
            controller.resetFromEntity();
        }
        var state = controller.state();

        // something outside the simulation set the velocity: knockback, an explosion, a bubble column...
        var velocity = player.getDeltaMovement();
        Optional<Vec3> impulse = Optional.empty();
        if (velocity.distanceToSqr(controller.writtenVelocity()) > IMPULSE_EPSILON * IMPULSE_EPSILON) {
            impulse = Optional.of(velocity);
            ArcadeSimulation.applyImpulse(state, velocity);
        }

        var from = player.position();
        var ctx = ArcadeSimulation.step(state, frame, controller.tuning(), controller.world(), new ArcadeReplayer.EntityBody(player));
        controller.remember(new ArcadeController.HistoryEntry(frame, from, ctx.result(), impulse));

        var written = new Vec3(state.vx, state.vy, state.vz);
        player.setDeltaMovement(written);
        controller.setWrittenVelocity(written);
        player.setSprinting(false);
        if (!frame.coupled()) {
            player.setYRot(state.facing);
        }
        if (player.isUsingItem() && controller.handsBusy()) {
            player.stopUsingItem();
        }
        return new Step(ctx, new ArcadeInputPayload(frame, from, ctx.result(), state.fingerprint(), impulse));
    }

    /**
     * Takes the state of the server as the truth as of the step it names, and replays every step taken
     * since, with the inputs and results remembered: the server does the same with the inputs still on
     * their way, so both agree again.
     */
    public static void correct(Player player, ArcadeController controller, ArcadeCorrectionPayload payload) {
        if (!controller.isDriving()) {
            return;
        }
        var state = payload.state().copy();
        if (!payload.rejected() && controller.profile() != null) {
            var tuning = controller.tuning();
            for (var entry : controller.history()) {
                if (entry.frame().tick() <= payload.tick()) {
                    continue;
                }
                entry.impulse().ifPresent(impulse -> ArcadeSimulation.applyImpulse(state, impulse));
                var ctx = ArcadeSimulation.plan(state, entry.frame(), tuning, controller.world(), entry.from());
                ArcadeSimulation.settle(ctx, entry.result());
            }
        } else {
            controller.history().clear();
        }
        state.events.clear();
        controller.setState(state);
        var velocity = new Vec3(state.vx, state.vy, state.vz);
        player.setDeltaMovement(velocity);
        controller.setWrittenVelocity(velocity);
    }

    /**
     * @param context what the simulation made of the tick
     * @param payload the report of the step to the server
     */
    public record Step(MoveContext context, ArcadeInputPayload payload) {
    }
}
