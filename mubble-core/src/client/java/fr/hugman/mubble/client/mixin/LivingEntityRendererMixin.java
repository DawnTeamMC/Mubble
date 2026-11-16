package fr.hugman.mubble.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.client.references.MubbleRenderStateDataKeys;
import fr.hugman.mubble.client.renderer.entity.CartoonPoseCallback;
import fr.hugman.mubble.client.renderer.entity.state.CartoonPose;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Unique
    private static final CartoonPose mubble$SCRATCH_POSE = new CartoonPose();

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void mubble$extractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (entity instanceof PowerUpHolder powerUpHolder) {
            powerUpHolder.getPowerUp().ifPresent(powerUp -> state.setData(MubbleRenderStateDataKeys.POWER_UP, powerUp));
        }
        CartoonPose pose = mubble$SCRATCH_POSE.reset();
        CartoonPoseCallback.EVENT.invoker().pose(entity, state, partialTicks, pose);
        // render states are pooled, so a neutral pose has to clear the key
        state.setData(MubbleRenderStateDataKeys.CARTOON_POSE, pose.isIdentity() ? null : pose.copy());
    }

    @Inject(method = "setupRotations", at = @At("RETURN"))
    private void mubble$applyCartoonPose(S state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci) {
        CartoonPose pose = state.getData(MubbleRenderStateDataKeys.CARTOON_POSE);
        if (pose != null) {
            pose.apply(poseStack);
        }
    }
}
