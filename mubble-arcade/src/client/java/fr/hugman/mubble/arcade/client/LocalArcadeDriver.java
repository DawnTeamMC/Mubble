package fr.hugman.mubble.arcade.client;

import fr.hugman.mubble.arcade.client.animation.ArcadePlayerAnimator;
import fr.hugman.mubble.arcade.client.camera.ArcadeCamera;
import fr.hugman.mubble.arcade.client.cue.ArcadeCuePlayer;
import fr.hugman.mubble.arcade.network.ArcadeCorrectionPayload;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadeLocalDriver;
import fr.hugman.mubble.arcade.ArcadePrediction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Drives the player of this client: every tick, the shared step runs in place of vanilla travel,
 * moves the player through vanilla collisions, and tells the server what it did.
 */
@Environment(EnvType.CLIENT)
public final class LocalArcadeDriver implements ArcadeLocalDriver {
    @Override
    public void travel(Player player, ArcadeController controller) {
        if (!(player instanceof LocalPlayer localPlayer)) {
            return;
        }
        int tick = controller.nextTick();
        var frame = ArcadeRecorder.replayFrame(tick).orElseGet(() -> ArcadeClientInput.frame(localPlayer, tick));
        var step = ArcadePrediction.step(player, controller, frame);
        ArcadeRecorder.onStep(localPlayer, step);
        if (!frame.coupled()) {
            ArcadeCamera.aimIfNeeded(Minecraft.getInstance(), localPlayer);
        }
        ClientPlayNetworking.send(step.payload());
        var state = controller.state();
        ArcadeCuePlayer.play(player, step.context().profile(), state.events);
        ArcadePlayerAnimator.onLocalStep(player, state);
    }

    public static void onCorrection(ArcadeCorrectionPayload payload) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            ArcadePrediction.correct(player, ArcadeController.of(player), payload);
        }
    }
}
