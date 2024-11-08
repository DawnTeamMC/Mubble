package fr.hugman.mubble.mixin.client;

import fr.hugman.mubble.client.render.entity.state.GoombaEntityRenderState;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Redirect(method = "setupTransforms", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;deathTime:F"))
    private float mubble$blockDeathAnimation(LivingEntityRenderState state) {
        if (state instanceof GoombaEntityRenderState goombaState && goombaState.stomped) {
            return 0;
        }
        return state.deathTime;
    }
}