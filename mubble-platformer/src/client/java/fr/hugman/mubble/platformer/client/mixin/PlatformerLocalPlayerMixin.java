package fr.hugman.mubble.platformer.client.mixin;

import fr.hugman.mubble.platformer.client.PlatformerClientInput;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import fr.hugman.mubble.platformer.PlatformerController;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class PlatformerLocalPlayerMixin {
    /**
     * With the orbit camera on, the hands reach out where the body faces, and never to a block;
     * aiming over the shoulder, the body faces the crosshair, and they reach out to it as vanilla's do.
     */
    @Inject(method = "raycastHitResult", at = @At("HEAD"), cancellable = true)
    private void mubble$pickFromTheOrbit(float partialTicks, Entity cameraEntity, CallbackInfoReturnable<HitResult> cir) {
        var player = (LocalPlayer) (Object) this;
        if (cameraEntity == player && !PlatformerCamera.coupled()) {
            cir.setReturnValue(PlatformerCamera.pick(player, partialTicks));
        }
    }

    /**
     * Vanilla crouches the local player whenever sneak is held, which is crouching, ground pounding,
     * rolling or diving for the platformer layer: while it drives, the player crouches when its move
     * does, as every other client sees it.
     */
    @Inject(method = "isCrouching", at = @At("HEAD"), cancellable = true)
    private void mubble$crouchWithTheMove(CallbackInfoReturnable<Boolean> cir) {
        var player = (LocalPlayer) (Object) this;
        if (PlatformerController.of(player).isDriving()) {
            cir.setReturnValue(player.hasPose(Pose.CROUCHING));
        }
    }

    /**
     * While the layer drives, the jump, sneak and sprint of the player are the platformer ones, whatever
     * keys they are on: vanilla's own keys stand aside, and what vanilla does with the input (the
     * server hearing of sneaking, starting to glide) follows the platformer keys.
     */
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = At.Shift.AFTER))
    private void mubble$platformerKeys(CallbackInfo ci) {
        var player = (LocalPlayer) (Object) this;
        if (PlatformerController.of(player).isDriving()) {
            player.input.keyPresses = PlatformerClientInput.standIn(player.input.keyPresses);
        }
    }

    /** Auto-jump would fight the platformer jumps: it is off while the layer drives the movement. */
    @Inject(method = "isAutoJumpEnabled", at = @At("HEAD"), cancellable = true)
    private void mubble$noAutoJump(CallbackInfoReturnable<Boolean> cir) {
        if (PlatformerController.of((LocalPlayer) (Object) this).isDriving()) {
            cir.setReturnValue(false);
        }
    }
}
