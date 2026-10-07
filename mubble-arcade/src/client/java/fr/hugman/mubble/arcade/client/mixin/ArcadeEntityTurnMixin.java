package fr.hugman.mubble.arcade.client.mixin;

import fr.hugman.mubble.arcade.client.camera.ArcadeCamera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * While the orbit camera is in use, whatever turns the view (the mouse, the right stick of a
 * controller) turns the camera instead of the player.
 */
@Mixin(Entity.class)
public class ArcadeEntityTurnMixin {
    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    private void mubble$turnTheOrbit(double xo, double yo, CallbackInfo ci) {
        if ((Object) this == Minecraft.getInstance().player && ArcadeCamera.isOrbiting()) {
            ArcadeCamera.turn(xo, yo);
            ci.cancel();
        }
    }
}
