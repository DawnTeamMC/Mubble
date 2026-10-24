package fr.hugman.mubble.platformer.client.mixin;

import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class PlatformerAbstractClientPlayerMixin {
    /** A small field of view kick with speed, smoothed by vanilla like the sprint one. */
    @Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
    private void mubble$speedKick(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof LocalPlayer player) {
            float kick = PlatformerCamera.fovKick(player);
            if (kick != 1.0F) {
                cir.setReturnValue(cir.getReturnValueF() * Mth.lerp(effectScale, 1.0F, kick));
            }
        }
    }
}
