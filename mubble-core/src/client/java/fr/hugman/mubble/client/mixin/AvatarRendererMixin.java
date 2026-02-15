package fr.hugman.mubble.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.client.renderer.entity.layers.PowerUpHumanoidLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    public AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "renderLeftHand", at = @At("TAIL"))
    private void mubble$renderLeftHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve, CallbackInfo ci) {
        var powerUp = Minecraft.getInstance().player.getPowerUp();
        var texture = powerUp.flatMap(powerUpHolder -> powerUpHolder.value().cosmectics().humanoidOverlayAssetId().map(id -> id.withPath(s -> "textures/" + s + ".png")));
        if(texture.isEmpty()) {
            return;
        }
        for (RenderLayer<AvatarRenderState, PlayerModel> layer : this.layers) {
            if(layer instanceof PowerUpHumanoidLayer<?, ?, ?> humanoidLayer) {
                var model = humanoidLayer.getModelSet().get(EquipmentSlot.CHEST);
                model.leftArm.resetPose();
                model.leftArm.visible = true;
                model.leftArm.zRot = -0.1F;
                submitNodeCollector.submitModelPart(model.leftArm, poseStack, RenderTypes.entityTranslucent(texture.get()), lightCoords, OverlayTexture.NO_OVERLAY, null);
            }
        }
    }

    @Inject(method = "renderRightHand", at = @At("TAIL"))
    private void mubble$renderRightHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve, CallbackInfo ci) {
        var powerUp = Minecraft.getInstance().player.getPowerUp();
        var texture = powerUp.flatMap(powerUpHolder -> powerUpHolder.value().cosmectics().humanoidOverlayAssetId().map(id -> id.withPath(s -> "textures/" + s + ".png")));
        if(texture.isEmpty()) {
            return;
        }
        for (RenderLayer<AvatarRenderState, PlayerModel> layer : this.layers) {
            if(layer instanceof PowerUpHumanoidLayer<?, ?, ?> humanoidLayer) {
                var model = humanoidLayer.getModelSet().get(EquipmentSlot.CHEST);
                model.rightArm.resetPose();
                model.rightArm.visible = true;
                model.rightArm.zRot = 0.1F;
                submitNodeCollector.submitModelPart(model.rightArm, poseStack, RenderTypes.entityTranslucent(texture.get()), lightCoords, OverlayTexture.NO_OVERLAY, null);
            }
        }
    }

    @Override
    @Shadow
    public Identifier getTextureLocation(AvatarRenderState state) {
        throw new IllegalStateException("Mixin failed to apply");
    }

    @Override
    @Shadow
    public AvatarRenderState createRenderState() {
        throw new IllegalStateException("Mixin failed to apply");
    }
}