package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.world.arcade.ArcadeController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ArcadeClientPacketListenerMixin {
    /** A teleport from the server starts the arcade movement over, from wherever it put the player. */
    @Inject(method = "handleMovePlayer", at = @At("TAIL"))
    private void mubble$restartArcadeMovement(ClientboundPlayerPositionPacket packet, CallbackInfo ci) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            ArcadeController.of(player).markReset();
        }
    }
}
