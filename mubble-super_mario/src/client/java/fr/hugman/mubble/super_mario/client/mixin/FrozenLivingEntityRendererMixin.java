package fr.hugman.mubble.super_mario.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderTypes;
import fr.hugman.mubble.super_mario.world.entity.freeze.FreezeSnapshot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Gives a frozen entity the colour of the ice it is caught in, and holds the one animation the age
 * alone does not drive.
 *
 * @see FrozenEntityRendererMixin
 */
@Mixin(LivingEntityRenderer.class)
@Environment(EnvType.CLIENT)
public class FrozenLivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Shadow
    public Identifier getTextureLocation(final S state) {
        return null;
    }

    /**
     * Puts the limbs back where they were the moment the ice took hold.
     * <p>
     * Vanilla reads them off a walk animation that runs itself down as soon as the entity stops
     * moving, so a mob frozen mid-stride would ease into a resting pose over the next half second.
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

    /**
     * Draws a frozen entity through the ice shader, which remaps it onto the ice block's palette.
     * <p>
     * Whatever vanilla settled on is kept when it decided not to draw the entity at all, so an
     * invisible mob stays invisible in there.
     */
    @ModifyReturnValue(method = "getRenderType", at = @At("RETURN"))
    private @Nullable RenderType super_mario$iceRenderTypeWhileFrozen(@Nullable RenderType original, S state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
        if (original == null || state.getData(SuperMarioRenderStateDataKeys.FREEZE) == null) {
            return original;
        }
        return SuperMarioRenderTypes.getFrozenEntity(this.getTextureLocation(state));
    }
}
