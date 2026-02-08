package fr.hugman.mubble.super_mario.client.mixin;

import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderTypes;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.GoombaRenderState;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Shadow
    public Identifier getTextureLocation(final S state) {
        return null;
    }

    @Redirect(method = "setupRotations", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;deathTime:F"))
    private float mubble$blockDeathAnimation(LivingEntityRenderState state) {
        if (state instanceof GoombaRenderState goombaState && goombaState.stomped) {
            return 0;
        }
        return state.deathTime;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void super_mario$extractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if(entity instanceof PowerUpHolder powerUpHolder) {
            powerUpHolder.getPowerUp().ifPresent(powerUpHolder1 -> state.setData(SuperMarioRenderStateDataKeys.POWER_UP, powerUpHolder1));
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void super_mario$getRenderType(S state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing, CallbackInfoReturnable<RenderType> cir) {
        var powerUp = state.getData(SuperMarioRenderStateDataKeys.POWER_UP);
        //TODO: make this more dynamic
        if(powerUp != null && powerUp.is(SuperMarioPowerUpKeys.GOLD)) {
            Identifier texture = this.getTextureLocation(state);
            //TODO: do the vanillas checks and add translucency support
            cir.setReturnValue(SuperMarioRenderTypes.getGoldenEntity(texture));
        }
    }
}