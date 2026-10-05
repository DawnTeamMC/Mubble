package fr.hugman.mubble.arcade.client.mixin;

import fr.hugman.mubble.arcade.client.animation.ArcadePlayerAnimator;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Plays the arcade animation of a player on its model, and on the armor models posed from the same
 * render state, once vanilla posed them. Without an arcade animation in the render state, nothing
 * happens.
 */
@Mixin(HumanoidModel.class)
public class ArcadeHumanoidModelMixin {
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void mubble$playArcadeAnimation(HumanoidRenderState state, CallbackInfo ci) {
        var data = state.getData(ArcadePlayerAnimator.DATA);
        if (data != null && data.isPlaying()) {
            ArcadePlayerAnimator.applyLimbs(((Model<?>) (Object) this).root(), data, state.ageInTicks);
        }
    }
}
