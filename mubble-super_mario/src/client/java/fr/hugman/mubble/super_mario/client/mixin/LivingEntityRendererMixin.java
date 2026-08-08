package fr.hugman.mubble.super_mario.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderTypes;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.GoombaRenderState;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys;
import fr.hugman.mubble.super_mario.world.entity.projectile.Bubble;
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
    private float super_mario$blockDeathAnimation(LivingEntityRenderState state) {
        if (state instanceof GoombaRenderState goombaState && goombaState.stomped) {
            return 0;
        }
        return state.deathTime;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void super_mario$extractBubbleRide(T entity, S state, float partialTicks, CallbackInfo ci) {
        // Render states are pooled, so the key has to be cleared for entities that are not in a bubble.
        if (!(entity.getVehicle() instanceof Bubble bubble)) {
            state.setData(SuperMarioRenderStateDataKeys.BUBBLE_RIDE, null);
            return;
        }
        float ticks = entity.tickCount + partialTicks;
        float seed = entity.getId() * 0.7F;
        float absorb = bubble.getAbsorbProgress(partialTicks);
        // Being swallowed adds a whirl on top of the idle tumble. It ramps up from zero so nothing snaps.
        float whirl = absorb * absorb * 6.0F;
        state.setData(SuperMarioRenderStateDataKeys.BUBBLE_RIDE, new SuperMarioRenderStateDataKeys.BubbleRide(
                ticks * 0.11F + seed + whirl,
                ticks * 0.077F + seed * 1.3F + whirl * 1.4F,
                1.0F - absorb
        ));
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    private void super_mario$bubbleRideRotations(S state, PoseStack poseStack, float bodyRot, float scale, CallbackInfo ci) {
        var ride = state.getData(SuperMarioRenderStateDataKeys.BUBBLE_RIDE);
        if (ride == null) {
            return;
        }
        // Tumble and shrink around the middle of the entity rather than around its feet.
        float centerY = state.boundingBoxHeight / 2.0F;
        poseStack.translate(0.0F, centerY, 0.0F);
        poseStack.mulPose(Axis.XP.rotation(ride.xRot()));
        poseStack.mulPose(Axis.ZP.rotation(ride.zRot()));
        poseStack.scale(ride.scale(), ride.scale(), ride.scale());
        poseStack.translate(0.0F, -centerY, 0.0F);
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void super_mario$getRenderType(S state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing, CallbackInfoReturnable<RenderType> cir) {
        var powerUp = state.getData(fr.hugman.mubble.client.references.MubbleRenderStateDataKeys.POWER_UP);
        //TODO: make this more dynamic
        if(powerUp != null && powerUp.is(SuperMarioPowerUpKeys.GOLD)) {
            Identifier texture = this.getTextureLocation(state);
            //TODO: do the vanillas checks and add translucency support
            cir.setReturnValue(SuperMarioRenderTypes.getGoldenEntity(texture));
        }
    }
}