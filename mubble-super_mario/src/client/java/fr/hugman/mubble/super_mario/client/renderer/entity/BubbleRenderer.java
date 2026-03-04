package fr.hugman.mubble.super_mario.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.client.model.BallModel;
import fr.hugman.mubble.client.renderer.entity.state.BallRenderState;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.world.entity.projectile.Bubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;

@Environment(EnvType.CLIENT)
public class BubbleRenderer extends EntityRenderer<Bubble, BallRenderState> {
    private final BallModel model;

    public BubbleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new BallModel(ctx.bakeLayer(SuperMarioModelLayers.BUBBLE));
    }

    @Override
    public BallRenderState createRenderState() {
        return new BallRenderState();
    }

    @Override
    public void extractRenderState(Bubble bubble, BallRenderState state, float f) {
        super.extractRenderState(bubble, state, f);
        state.texture = bubble.getTexture();
        state.lightCoords = LightCoordsUtil.FULL_BRIGHT;
    }

    @Override
    public void submit(BallRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        var size = 4;
        poseStack.scale(state.boundingBoxWidth * size, state.boundingBoxHeight * size, state.boundingBoxWidth * size);
        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entityTranslucent(state.texture.texturePath()), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, cameraRenderState);
    }
}
