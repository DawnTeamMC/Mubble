package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.renderer.entity.state.GoombaRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Redirect(method = "setupRotations", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;deathTime:F"))
    private float mubble$blockDeathAnimation(LivingEntityRenderState state) {
        if (state instanceof GoombaRenderState goombaState && goombaState.stomped) {
            return 0;
        }
        return state.deathTime;
    }
}