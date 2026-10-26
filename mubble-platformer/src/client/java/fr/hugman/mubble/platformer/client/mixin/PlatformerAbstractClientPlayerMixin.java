package fr.hugman.mubble.platformer.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public class PlatformerAbstractClientPlayerMixin {
    /** A small field of view kick with speed, smoothed by vanilla like the sprint one. */
    @ModifyReturnValue(method = "getFieldOfViewModifier", at = @At("RETURN"))
    private float mubble$speedKick(float modifier, boolean firstPerson, float effectScale) {
        if ((Object) this instanceof LocalPlayer player) {
            float kick = PlatformerCamera.fovKick(player);
            if (kick != 1.0F) {
                return modifier * Mth.lerp(effectScale, 1.0F, kick);
            }
        }
        return modifier;
    }
}
