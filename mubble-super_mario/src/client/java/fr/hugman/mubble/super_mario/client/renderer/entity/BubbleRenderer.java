package fr.hugman.mubble.super_mario.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.BubbleRenderState;
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
public class BubbleRenderer extends EntityRenderer<Bubble, BubbleRenderState> {

    public BubbleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public BubbleRenderState createRenderState() {
        return new BubbleRenderState();
    }

    @Override
    public void extractRenderState(Bubble bubble, BubbleRenderState state, float f) {
        super.extractRenderState(bubble, state, f);
        state.texture = bubble.getTexture();
        state.lightCoords = LightCoordsUtil.FULL_BRIGHT;
    }

    @Override
    public void submit(BubbleRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(cameraRenderState.orientation);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        int light = state.lightCoords;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(state.texture.texturePath()), (pose, consumer) -> {
            vertex(consumer, pose, -0.5f, -0.5f, 0.0f, 1.0f, light);
            vertex(consumer, pose, 0.5f, -0.5f, 1.0f, 1.0f, light);
            vertex(consumer, pose, 0.5f, 0.5f, 1.0f, 0.0f, light);
            vertex(consumer, pose, -0.5f, 0.5f, 0.0f, 0.0f, light);
        });
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, cameraRenderState);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v, int light) {
        consumer.addVertex(pose, x, y, 0.0f)
                .setColor(255, 255, 255, 200)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
