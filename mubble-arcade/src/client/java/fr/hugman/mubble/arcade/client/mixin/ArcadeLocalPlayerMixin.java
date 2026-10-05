package fr.hugman.mubble.arcade.client.mixin;

import fr.hugman.mubble.arcade.client.camera.ArcadeCamera;
import fr.hugman.mubble.arcade.ArcadeController;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class ArcadeLocalPlayerMixin {
    /** With the orbit camera on, the hands reach out where the body faces, and never to a block. */
    @Inject(method = "raycastHitResult", at = @At("HEAD"), cancellable = true)
    private void mubble$pickFromTheOrbit(float partialTicks, Entity cameraEntity, CallbackInfoReturnable<HitResult> cir) {
        var player = (LocalPlayer) (Object) this;
        if (cameraEntity == player && ArcadeCamera.isOrbiting()) {
            cir.setReturnValue(ArcadeCamera.pick(player, partialTicks));
        }
    }

    /**
     * Vanilla crouches the local player whenever sneak is held, which is crouching, ground pounding,
     * rolling or diving for the arcade layer: while it drives, the player crouches when its move
     * does, as every other client sees it.
     */
    @Inject(method = "isCrouching", at = @At("HEAD"), cancellable = true)
    private void mubble$crouchWithTheMove(CallbackInfoReturnable<Boolean> cir) {
        var player = (LocalPlayer) (Object) this;
        if (ArcadeController.of(player).isDriving()) {
            cir.setReturnValue(player.hasPose(Pose.CROUCHING));
        }
    }

    /** Auto-jump would fight the arcade jumps: it is off while the layer drives the movement. */
    @Inject(method = "isAutoJumpEnabled", at = @At("HEAD"), cancellable = true)
    private void mubble$noAutoJump(CallbackInfoReturnable<Boolean> cir) {
        if (ArcadeController.of((LocalPlayer) (Object) this).isDriving()) {
            cir.setReturnValue(false);
        }
    }
}
