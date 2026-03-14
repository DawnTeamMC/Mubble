package fr.hugman.mubble.client.renderer.entity;

import fr.hugman.mubble.client.model.BallModel;
import fr.hugman.mubble.client.model.MubbleModelLayers;
import fr.hugman.mubble.client.renderer.entity.state.BallRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.world.entity.projectile.Ball;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;

@Environment(EnvType.CLIENT)
public class BallRenderer extends EntityRenderer<Ball, BallRenderState> {
    private final BallModel model;

    public BallRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new BallModel(ctx.bakeLayer(MubbleModelLayers.BALL));
    }

    @Override
    public BallRenderState createRenderState() {
        return new BallRenderState();
    }

    @Override
    public void extractRenderState(Ball ball, BallRenderState state, float f) {
        super.extractRenderState(ball, state, f);
        state.xRot = ball.getXRot(f);
        state.yRot = ball.getYRot(f);
        state.texture = ball.getTexture();
        state.lightCoords = LightCoordsUtil.FULL_BRIGHT;
        state.rotateClockwards = ball.rotatesClockwards();
    }

    @Override
    public void submit(BallRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + 180.0F));
		poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.ageInTicks * (state.rotateClockwards ? -20.0F : 20.0F)));
		var size = 4;
		poseStack.scale(state.boundingBoxWidth * size, state.boundingBoxHeight * size, state.boundingBoxWidth * size);
		submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entityCutout(state.texture.texturePath()), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, cameraRenderState);
    }
}