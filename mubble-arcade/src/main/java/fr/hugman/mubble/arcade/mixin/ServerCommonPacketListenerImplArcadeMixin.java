package fr.hugman.mubble.arcade.mixin;

import fr.hugman.mubble.arcade.ArcadeController;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Watches what the server sends a player about their own motion: a push the client will report back
 * as a change of velocity it did not simulate, or a teleport that starts its movement over.
 */
@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonPacketListenerImplArcadeMixin {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V", at = @At("HEAD"))
    private void mubble$watchArcadeMotion(Packet<?> packet, @Nullable ChannelFutureListener listener, CallbackInfo ci) {
        if ((Object) this instanceof ServerGamePacketListenerImpl game && game.player != null) {
            mubble$watch(game.player, packet);
        }
    }

    @Unique
    private static void mubble$watch(ServerPlayer player, Packet<?> packet) {
        var controller = ArcadeController.of(player);
        if (!controller.isActive()) {
            return;
        }
        var validation = controller.validation();
        long time = player.level().getGameTime();
        // plain instanceof checks rather than a pattern switch, which mixins do not merge well
        if (packet instanceof ClientboundSetEntityMotionPacket motion) {
            if (motion.id() == player.getId()) {
                validation.recordImpulse(time, motion.movement().length());
            }
        } else if (packet instanceof ClientboundExplodePacket explosion) {
            explosion.playerKnockback().ifPresent(knockback -> validation.recordImpulse(time, knockback.length()));
        } else if (packet instanceof ClientboundPlayerPositionPacket) {
            validation.expectedPosition = null;
            if (!validation.ownTeleport) {
                controller.markReset();
            }
        } else if (packet instanceof ClientboundBundlePacket bundle) {
            bundle.subPackets().forEach(sub -> mubble$watch(player, sub));
        }
    }
}
