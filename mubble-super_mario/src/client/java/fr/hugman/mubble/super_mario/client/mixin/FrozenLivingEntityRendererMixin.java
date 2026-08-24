package fr.hugman.mubble.super_mario.client.mixin;

import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Gives a frozen entity the colour of the ice it is caught in, and cuts short the one animation the
 * age alone does not drive.
 *
 * @see FrozenEntityRendererMixin
 */
@Mixin(LivingEntityRenderer.class)
@Environment(EnvType.CLIENT)
public class FrozenLivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    /** The pale blue of the ice block, which whatever is seen through it takes on. */
    private static final int super_mario$ICE_TINT = 0xFF8FC7F0;

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void super_mario$stillWhileFrozen(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (state.getData(SuperMarioRenderStateDataKeys.FREEZE) != null) {
            // a shoved block of ice still travels, and its passenger must not look like it is walking
            state.walkAnimationSpeed = 0.0F;
        }
    }

    @Inject(method = "getModelTint", at = @At("RETURN"), cancellable = true)
    private void super_mario$tintWhileFrozen(S state, CallbackInfoReturnable<Integer> cir) {
        if (state.getData(SuperMarioRenderStateDataKeys.FREEZE) != null) {
            cir.setReturnValue(ARGB.multiply(cir.getReturnValueI(), super_mario$ICE_TINT));
        }
    }
}
