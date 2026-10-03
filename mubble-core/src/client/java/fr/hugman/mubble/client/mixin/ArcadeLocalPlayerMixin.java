package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.arcade.camera.ArcadeCamera;
import fr.hugman.mubble.world.arcade.ArcadeController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class ArcadeLocalPlayerMixin {
    /** The crosshair of the orbit camera, checked from the eyes of the player. */
    @Inject(method = "raycastHitResult", at = @At("HEAD"), cancellable = true)
    private void mubble$pickFromTheOrbit(float partialTicks, Entity cameraEntity, CallbackInfoReturnable<HitResult> cir) {
        var player = (LocalPlayer) (Object) this;
        if (cameraEntity == player && ArcadeCamera.isOrbiting()) {
            cir.setReturnValue(ArcadeCamera.pick(Minecraft.getInstance(), player, partialTicks));
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
