package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.references.MubbleRenderStateDataKeys;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void mubble$extractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (entity instanceof PowerUpHolder powerUpHolder) {
            powerUpHolder.getPowerUp().ifPresent(powerUp -> state.setData(MubbleRenderStateDataKeys.POWER_UP, powerUp));
        }
    }
}