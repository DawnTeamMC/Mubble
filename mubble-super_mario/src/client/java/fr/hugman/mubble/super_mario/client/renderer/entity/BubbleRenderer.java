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
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;

@Environment(EnvType.CLIENT)
public class BubbleRenderer extends EntityRenderer<Bubble, BubbleRenderState> {
    /** How much the bubble flattens along the axis it hit, at the peak of the squish. */
    private static final float SQUISH_FLATTEN = 0.35F;
    /** How much it bulges along the other axis, to keep it looking like it conserves its volume. */
    private static final float SQUISH_BULGE = 0.25F;

    public BubbleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public BubbleRenderState createRenderState() {
        return new BubbleRenderState();
    }

    @Override
    public void extractRenderState(Bubble bubble, BubbleRenderState state, float partialTicks) {
        super.extractRenderState(bubble, state, partialTicks);
        state.texture = bubble.getTexture();
        state.size = bubble.getRenderSize(partialTicks);
        state.lightCoords = LightCoordsUtil.FULL_BRIGHT;
        state.squish = bubble.getSquish(partialTicks);
        state.squishAxis = bubble.getSquishAxis();
        state.captureWobble = bubble.getCaptureWobble(partialTicks);
    }

    @Override
    public void submit(BubbleRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        // The entity origin sits at the bottom of the cube, the sprite has to be centered on it.
        float centerY = state.size / 2.0F;

        // Whatever the bubble holds is a passenger, so the entity renderer draws it on its own.
        poseStack.pushPose();
        poseStack.translate(0.0F, centerY, 0.0F);
        poseStack.mulPose(cameraRenderState.orientation);

        // Squishing happens in camera space: the quad is a billboard, so this reads as a screen-space squash.
        float flatten = 1.0F - SQUISH_FLATTEN * state.squish;
        float bulge = 1.0F + SQUISH_BULGE * state.squish;
        boolean vertical = state.squishAxis == Direction.Axis.Y;
        // Closing around something squashes the bubble on the spot, on top of any block rebound. It keeps
        // roughly the same volume, so the sides push out while the top comes down and the other way round.
        float wobbleY = 1.0F + state.captureWobble;
        float wobbleX = 1.0F - state.captureWobble * 0.5F;
        poseStack.scale(
                state.size * (vertical ? bulge : flatten) * wobbleX,
                state.size * (vertical ? flatten : bulge) * wobbleY,
                state.size * wobbleX
        );

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
                // Written straight through instead of through the pose: the pose carries the billboard's
                // camera orientation, so a transformed normal swings around with the camera and the entity
                // shader dims the sprite accordingly -- most visibly when looking straight down. A fixed
                // upright normal keeps the shading even from every angle.
                .setNormal(0.0f, 1.0f, 0.0f);
    }
}
