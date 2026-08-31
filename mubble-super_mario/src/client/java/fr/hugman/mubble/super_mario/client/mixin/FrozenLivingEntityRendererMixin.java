package fr.hugman.mubble.super_mario.client.mixin;

import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.world.entity.freeze.FreezeSnapshot;
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
 * Gives a frozen entity the colour of the ice it is caught in, and holds the one animation the age
 * alone does not drive.
 *
 * @see FrozenEntityRendererMixin
 */
@Mixin(LivingEntityRenderer.class)
@Environment(EnvType.CLIENT)
public class FrozenLivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    /** The pale blue of the ice block, which whatever is seen through it takes on. */
    private static final int super_mario$ICE_TINT = 0xFFB9E4FF;

    /**
     * Puts the limbs back where they were the moment the ice took hold.
     * <p>
     * Vanilla reads them off a walk animation that runs itself down as soon as the entity stops
     * moving, so a mob frozen mid-stride would ease into a resting pose over the next half second.
     * The snapshot the entity took of itself does not move, and neither do the limbs read off it.
     */
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void super_mario$holdThePoseWhileFrozen(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (state.getData(SuperMarioRenderStateDataKeys.FREEZE) == null) {
            return;
        }
        var snapshot = (FreezeSnapshot) entity;
        state.walkAnimationPos = snapshot.frozenWalkPos();
        state.walkAnimationSpeed = snapshot.frozenWalkSpeed();
    }

    @Inject(method = "getModelTint", at = @At("RETURN"), cancellable = true)
    private void super_mario$tintWhileFrozen(S state, CallbackInfoReturnable<Integer> cir) {
        if (state.getData(SuperMarioRenderStateDataKeys.FREEZE) != null) {
            cir.setReturnValue(ARGB.multiply(cir.getReturnValueI(), super_mario$ICE_TINT));
        }
    }
}
