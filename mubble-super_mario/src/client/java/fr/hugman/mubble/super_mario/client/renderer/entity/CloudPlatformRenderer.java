package fr.hugman.mubble.super_mario.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.client.model.CloudPlatformModel;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.CloudPlatformRenderState;
import fr.hugman.mubble.super_mario.world.entity.platform.CloudPlatform;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class CloudPlatformRenderer extends EntityRenderer<CloudPlatform, CloudPlatformRenderState> {
    private static final int SQUISH_DURATION = 10;
    private static final float SQUISH_INTENSITY = .3f;
    private static final Identifier DEFAULT_TEXTURE = Identifier.withDefaultNamespace("textures/block/white_concrete.png");

    private final CloudPlatformModel model;

    public CloudPlatformRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CloudPlatformModel(context.bakeLayer(SuperMarioModelLayers.CLOUD_PLATFORM));
    }

    @Override
    public CloudPlatformRenderState createRenderState() {
        return new CloudPlatformRenderState();
    }

    @Override
    public void extractRenderState(CloudPlatform entity, CloudPlatformRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.scale = entity.getScale(partialTicks);
        state.squishProgress = Math.min(1.0f, (entity.getTicksSinceLastOccupied() + partialTicks) / (float) SQUISH_DURATION);
    }

    @Override
    public void submit(CloudPlatformRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        if (state.squishProgress < 1.0f) {
            float squishFactor = (float) Math.sin(state.squishProgress * Math.PI * 6) * SQUISH_INTENSITY * (1.0f - state.squishProgress);
            float scaleY = 1.0f + squishFactor;
            float scaleXZ = 1.0f - (squishFactor * 0.5f);
            poseStack.scale(scaleXZ, scaleY, scaleXZ);
        }

        poseStack.translate(0, (1 - state.scale) / 2, 0);
        poseStack.scale(state.scale, state.scale, state.scale);

        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entityCutout(DEFAULT_TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
