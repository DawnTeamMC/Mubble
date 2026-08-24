package fr.hugman.mubble.super_mario.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.FreezeRenderData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Wraps a frozen entity in the block of ice holding it, and holds its animations still while it is
 * in there.
 */
@Mixin(EntityRenderer.class)
@Environment(EnvType.CLIENT)
public class FrozenEntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V", at = @At("TAIL"))
    private void super_mario$extractFreeze(T entity, S state, float partialTicks, CallbackInfo ci) {
        var freeze = FreezeRenderData.of(entity, partialTicks);
        // set even when absent: render states are handed down from one entity to the next
        state.setData(SuperMarioRenderStateDataKeys.FREEZE, freeze);
        if (freeze != null) {
            // winding the age back to what it was when the ice took hold stops everything driven by it
            state.ageInTicks -= freeze.frozenFor();
        }
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void super_mario$submitIceCube(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        var freeze = state.getData(SuperMarioRenderStateDataKeys.FREEZE);
        if (freeze == null) {
            return;
        }
        poseStack.pushPose();
        // the block model spans a whole block from the corner it is drawn at, hence the centering
        poseStack.scale(state.boundingBoxWidth, state.boundingBoxHeight, state.boundingBoxWidth);
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        submitNodeCollector.submitMovingBlock(poseStack, freeze.iceCube(), 0);
        poseStack.popPose();
    }
}
