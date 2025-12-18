package fr.hugman.mubble.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.client.render.entity.model.BallEntityModel;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.client.render.entity.state.BallRenderState;
import fr.hugman.mubble.entity.BallEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class BallEntityRenderer extends EntityRenderer<BallEntity, BallRenderState> {
    private final BallEntityModel model;

    public BallEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new BallEntityModel(ctx.bakeLayer(MubbleModelLayers.BALL));
    }

    @Override
    public BallRenderState createRenderState() {
        return new BallRenderState();
    }

    @Override
    public void extractRenderState(BallEntity ball, BallRenderState state, float f) {
        super.extractRenderState(ball, state, f);
        state.xRot = ball.getXRot(f);
        state.yRot = ball.getYRot(f);
        state.texture = ball.getTexture();
        state.lightCoords = 15728880;
    }

    @Override
    public void submit(BallRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(entityRenderState.yRot - 90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(entityRenderState.xRot));
        var size = 4;
        poseStack.scale(entityRenderState.boundingBoxWidth * size, entityRenderState.boundingBoxHeight * size, entityRenderState.boundingBoxWidth * size);
        submitNodeCollector.submitModel(this.model, entityRenderState, poseStack, RenderTypes.entityCutout(entityRenderState.texture.texturePath()), entityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, entityRenderState.outlineColor, null);
        poseStack.popPose();

        super.submit(entityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }
}