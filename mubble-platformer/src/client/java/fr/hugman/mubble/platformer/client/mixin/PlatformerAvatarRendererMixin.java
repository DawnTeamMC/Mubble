package fr.hugman.mubble.platformer.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.platformer.client.animation.PlatformerPlayerAnimator;
import fr.hugman.mubble.platformer.client.animation.PlatformerRenderData;
import fr.hugman.mubble.platformer.client.camera.PlatformerSilhouette;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Carries the platformer animation of a player into its render state, and moves the whole body for the
 * moves animating it. The data is set on every extraction, to {@link PlatformerRenderData#NONE} outside
 * platformer moves, so that nothing lingers from one frame to the next.
 */
@Mixin(AvatarRenderer.class)
public class PlatformerAvatarRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void mubble$extractPlatformerAnimation(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        var data = entity instanceof Player player ? PlatformerPlayerAnimator.renderData(player) : PlatformerRenderData.NONE;
        state.setData(PlatformerPlayerAnimator.DATA, data);
        state.setData(PlatformerSilhouette.SHOWN, PlatformerSilhouette.shown(entity));
        if (data.drivesBody()) {
            // the move turns the whole body itself: vanilla's swimming tilt would only add to it
            state.swimAmount = 0.0F;
            state.isVisuallySwimming = false;
        }
    }

    @Inject(method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V", at = @At("TAIL"))
    private void mubble$rotatePlatformerBody(AvatarRenderState state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci) {
        var data = state.getData(PlatformerPlayerAnimator.DATA);
        if (data != null && data.drivesBody()) {
            // vanilla turned the body the way it faces, and shook it or turned it upside down as it does any
            // player's, without its swimming tilt, left out on extraction: the whole body of the move goes on top
            PlatformerPlayerAnimator.applyBody(poseStack, data, state.ageInTicks);
        }
    }
}
