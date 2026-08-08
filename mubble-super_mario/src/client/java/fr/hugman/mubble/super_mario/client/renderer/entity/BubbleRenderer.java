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
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;

@Environment(EnvType.CLIENT)
public class BubbleRenderer extends EntityRenderer<Bubble, BubbleRenderState> {
    /** How much the bubble flattens along the axis it hit, at the peak of the squish. */
    private static final float SQUISH_FLATTEN = 0.35F;
    /** How much it bulges along the other axis, to keep it looking like it conserves its volume. */
    private static final float SQUISH_BULGE = 0.25F;
    /** The item held inside sits at a fraction of the bubble size, so it never pokes through the sprite. */
    private static final float HELD_ITEM_SCALE = 0.6F;

    private final ItemModelResolver itemModelResolver;

    public BubbleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemModelResolver = ctx.getItemModelResolver();
    }

    @Override
    public BubbleRenderState createRenderState() {
        return new BubbleRenderState();
    }

    @Override
    public void extractRenderState(Bubble bubble, BubbleRenderState state, float partialTicks) {
        super.extractRenderState(bubble, state, partialTicks);
        state.texture = bubble.getTexture();
        state.lightCoords = LightCoordsUtil.FULL_BRIGHT;
        state.squish = bubble.getSquish(partialTicks);
        state.squishAxis = bubble.getSquishAxis();
        this.itemModelResolver.updateForNonLiving(state.item, bubble.getItem(), ItemDisplayContext.GROUND, bubble);
    }

    @Override
    public void submit(BubbleRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        // The entity origin sits at the bottom of the bounding box, the sprite has to be centered on it.
        float centerY = state.boundingBoxHeight / 2.0F;

        if (!state.item.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0F, centerY, 0.0F);
            poseStack.mulPose(cameraRenderState.orientation);
            float itemScale = Math.min(state.boundingBoxWidth, state.boundingBoxHeight) * HELD_ITEM_SCALE;
            poseStack.scale(itemScale, itemScale, itemScale);
            state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }

        // The sprite is translucent, so it is drawn after whatever it contains.
        poseStack.pushPose();
        poseStack.translate(0.0F, centerY, 0.0F);
        poseStack.mulPose(cameraRenderState.orientation);

        // Squishing happens in camera space: the quad is a billboard, so this reads as a screen-space squash.
        float flatten = 1.0F - SQUISH_FLATTEN * state.squish;
        float bulge = 1.0F + SQUISH_BULGE * state.squish;
        boolean vertical = state.squishAxis == Direction.Axis.Y;
        poseStack.scale(
                state.boundingBoxWidth * (vertical ? bulge : flatten),
                state.boundingBoxHeight * (vertical ? flatten : bulge),
                state.boundingBoxWidth
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
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
