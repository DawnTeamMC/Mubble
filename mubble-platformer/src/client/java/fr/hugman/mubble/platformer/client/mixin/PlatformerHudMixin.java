package fr.hugman.mubble.platformer.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class PlatformerHudMixin {
    /** Vanilla only shows the crosshair in the first person view: aiming over the shoulder shows it too. */
    @ModifyExpressionValue(method = "extractCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
    private boolean mubble$crosshairOverTheShoulder(boolean firstPerson) {
        return firstPerson || PlatformerCamera.showsCrosshair();
    }
}
