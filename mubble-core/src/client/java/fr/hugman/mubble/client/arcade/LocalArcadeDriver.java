package fr.hugman.mubble.client.arcade;

import fr.hugman.mubble.client.arcade.animation.ArcadePlayerAnimator;
import fr.hugman.mubble.client.arcade.camera.ArcadeCamera;
import fr.hugman.mubble.client.arcade.cue.ArcadeCuePlayer;
import fr.hugman.mubble.network.protocol.common.custom.ArcadeCorrectionPayload;
import fr.hugman.mubble.network.protocol.common.custom.ArcadeInputPayload;
import fr.hugman.mubble.world.arcade.ArcadeController;
import fr.hugman.mubble.world.arcade.ArcadeLocalDriver;
import fr.hugman.mubble.world.arcade.replay.ArcadeReplayer;
import fr.hugman.mubble.world.arcade.sim.ArcadeSimulation;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Drives the player of this client: every tick, the shared step runs in place of vanilla travel,
 * moves the player through vanilla collisions, and tells the server what it did.
 */
@Environment(EnvType.CLIENT)
public final class LocalArcadeDriver implements ArcadeLocalDriver {
    /** Below this, a difference between the velocity written last tick and the one found now is rounding, not a push. */
    private static final double IMPULSE_EPSILON = 1.0E-6D;

    @Override
    public void travel(Player player, ArcadeController controller) {
        if (!(player instanceof LocalPlayer localPlayer)) {
            return;
        }
        if (controller.needsReset()) {
            controller.resetFromEntity();
        }
        var state = controller.state();

        // something outside the simulation set the velocity: knockback, an explosion, a bubble column...
        var velocity = player.getDeltaMovement();
        Optional<Vec3> impulse = Optional.empty();
        if (velocity.distanceToSqr(controller.writtenVelocity()) > IMPULSE_EPSILON * IMPULSE_EPSILON) {
            impulse = Optional.of(velocity);
            state.vx = velocity.x;
            state.vy = velocity.y;
            state.vz = velocity.z;
        }

        var frame = ArcadeClientInput.frame(localPlayer, controller.nextTick());
        var from = player.position();
        var ctx = ArcadeSimulation.step(state, frame, controller.tuning(), controller.world(), new ArcadeReplayer.EntityBody(player));
        controller.remember(new ArcadeController.HistoryEntry(frame, from, ctx.result(), impulse));

        var written = new Vec3(state.vx, state.vy, state.vz);
        player.setDeltaMovement(written);
        controller.setWrittenVelocity(written);
        player.setSprinting(false);
        if (!frame.coupled()) {
            player.setYRot(state.facing);
            ArcadeCamera.aimIfNeeded(Minecraft.getInstance(), localPlayer);
        }
        if (player.isUsingItem() && controller.handsBusy()) {
            player.stopUsingItem();
        }

        ClientPlayNetworking.send(new ArcadeInputPayload(frame, from, ctx.result(), state.fingerprint(), impulse));
        ArcadeCuePlayer.play(player, ctx.profile(), state.events);
        ArcadePlayerAnimator.onLocalStep(player, state);
    }

    /**
     * Takes the state of the server as the truth as of the step it names, and replays every step taken
     * since, with the inputs and results remembered: the server does the same with the inputs still on
     * their way, so both agree again.
     */
    public static void onCorrection(ArcadeCorrectionPayload payload) {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        var controller = ArcadeController.of(player);
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
                entry.impulse().ifPresent(impulse -> {
                    state.vx = impulse.x;
                    state.vy = impulse.y;
                    state.vz = impulse.z;
                });
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
}
