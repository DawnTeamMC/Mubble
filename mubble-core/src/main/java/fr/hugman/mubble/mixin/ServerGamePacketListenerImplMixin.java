package fr.hugman.mubble.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.hugman.mubble.world.arcade.ArcadeController;
import fr.hugman.mubble.world.arcade.server.ArcadeValidator;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hands two of the vanilla movement checks over to the arcade validator, for the packets a validated
 * step announced and those only: "moved too quickly" and the floating kick. Every other packet, and
 * every other player, goes through vanilla untouched.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    /** Whether the move packet being handled was announced by a validated arcade step. */
    @Unique
    private boolean mubble$vouched;

    @Inject(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER))
    private void mubble$checkArcadeStep(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        this.mubble$vouched = false;
        var controller = ArcadeController.of(this.player);
        if (!packet.hasPosition() || !controller.isDriving()) {
            return;
        }
        if (controller.validation().expectedPosition == null) {
            // a move no step announced: vanilla checks it, and the floating kick counts again
            controller.validation().vouched = false;
            return;
        }
        // a mismatch sends the player back, after which vanilla ignores the packet while it waits for the teleport to be accepted
        this.mubble$vouched = ArcadeValidator.checkMovePacket(this.player, packet.getX(this.player.getX()), packet.getY(this.player.getY()), packet.getZ(this.player.getZ()));
    }

    @ModifyExpressionValue(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;shouldCheckPlayerMovement(Z)Z"))
    private boolean mubble$leaveSpeedToTheValidator(boolean original) {
        return original && !this.mubble$vouched;
    }

    @WrapOperation(method = "handleMovePlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;clientIsFloating:Z", opcode = org.objectweb.asm.Opcodes.PUTFIELD))
    private void mubble$leaveFloatingToTheValidator(ServerGamePacketListenerImpl listener, boolean floating, Operation<Void> original) {
        // the validator replays gravity every tick: a hang or a slide it let through is no flight
        var controller = ArcadeController.of(this.player);
        boolean vouched = this.mubble$vouched || (controller.isDriving() && controller.validation().vouched);
        original.call(listener, floating && !vouched);
    }
}
