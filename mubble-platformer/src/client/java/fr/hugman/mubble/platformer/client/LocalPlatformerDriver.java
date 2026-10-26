package fr.hugman.mubble.platformer.client;

import fr.hugman.mubble.platformer.client.animation.PlatformerPlayerAnimator;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import fr.hugman.mubble.platformer.client.cue.PlatformerCuePlayer;
import fr.hugman.mubble.platformer.network.PlatformerCorrectionPayload;
import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.PlatformerLocalDriver;
import fr.hugman.mubble.platformer.PlatformerPrediction;
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
public final class LocalPlatformerDriver implements PlatformerLocalDriver {
    @Override
    public void travel(Player player, PlatformerController controller) {
        if (!(player instanceof LocalPlayer localPlayer)) {
            return;
        }
        int tick = controller.nextTick();
        var frame = PlatformerRecorder.replayFrame(tick).orElseGet(() -> PlatformerClientInput.frame(localPlayer, tick));
        var step = PlatformerPrediction.step(player, controller, frame);
        PlatformerRecorder.onStep(localPlayer, step);
        if (!frame.coupled()) {
            PlatformerCamera.levelHead(localPlayer);
        }
        ClientPlayNetworking.send(step.payload());
        var state = controller.state();
        PlatformerCuePlayer.play(player, step.context().profile(), state.events);
        PlatformerPlayerAnimator.onLocalStep(player, state);
    }

    public static void onCorrection(PlatformerCorrectionPayload payload) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            PlatformerPrediction.correct(player, PlatformerController.of(player), payload);
        }
    }
}
