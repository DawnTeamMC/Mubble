package fr.hugman.mubble.arcade.mixin;

import fr.hugman.mubble.arcade.network.ArcadeInputPayload;
import fr.hugman.mubble.arcade.server.ArcadeValidator;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps the arcade input of a tick in front of the vanilla move packet of the same tick.
 * <p>
 * Custom payloads are normally handed to the server thread as tasks, which it runs whenever it waits
 * for the next tick, while vanilla packets wait for the start of the tick in a queue of their own: a
 * burst of two ticks would be validated in the wrong order. Arcade inputs are put in the vanilla
 * queue instead, right where they arrived, so that every step is validated just before the move
 * packet it announces.
 */
@Mixin(ServerboundCustomPayloadPacket.class)
public class ServerboundCustomPayloadPacketMixin {
    @Inject(method = "handle(Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;)V", at = @At("HEAD"), cancellable = true)
    private void mubble$handleArcadeInputInOrder(ServerCommonPacketListener listener, CallbackInfo ci) {
        var packet = (ServerboundCustomPayloadPacket) (Object) this;
        if (packet.payload() instanceof ArcadeInputPayload payload && listener instanceof ServerGamePacketListenerImpl game) {
            PacketUtils.ensureRunningOnSameThread(packet, listener, game.player.level().getServer().packetProcessor());
            ArcadeValidator.handleInput(game.player, payload);
            ci.cancel();
        }
    }
}
