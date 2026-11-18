package fr.hugman.mubble.super_mario.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.super_mario.client.model.FlowerModel;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.FlowerRenderState;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.projectile.Flower;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Draws the huge flower, spinning slowly as it grows.
 */
public class FlowerRenderer extends EntityRenderer<Flower, FlowerRenderState> {
    private static final Identifier TEXTURE = SuperMario.id("textures/entity/flower.png");
    /** How far the flower turns over one block travelled, in degrees. */
    private static final float SPIN_PER_BLOCK = 40.0F;
    /** How much of its height the flower loses at the peak of the squish of a bounce. */
    private static final float SQUISH_FLATTEN = 0.35F;
    /** How much wider it goes at the same time, so that the squash keeps its bulk. */
    private static final float SQUISH_BULGE = 0.2F;

    private final FlowerModel model;

    public FlowerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FlowerModel(context.bakeLayer(SuperMarioModelLayers.FLOWER));
    }

    @Override
    public FlowerRenderState createRenderState() {
        return new FlowerRenderState();
    }

    @Override
    public void extractRenderState(Flower entity, FlowerRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.travelled = (float) (entity.getTravelled() + entity.getSpeed() * partialTicks);
        state.squish = entity.getSquish(partialTicks);
    }

    @Override
    public void submit(FlowerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(state.travelled * SPIN_PER_BLOCK)));
        // Squashed against the ceiling it hit and spreading sideways, easing back out over the next few ticks.
        if (state.squish > 0.0F) {
            poseStack.scale(1.0F + SQUISH_BULGE * state.squish, 1.0F - SQUISH_FLATTEN * state.squish, 1.0F + SQUISH_BULGE * state.squish);
        }
        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entityCutout(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
