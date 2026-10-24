package fr.hugman.mubble.splatoon.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.client.model.InkBulletModel;
import fr.hugman.mubble.splatoon.client.model.SplatoonModelLayers;
import fr.hugman.mubble.splatoon.client.renderer.entity.state.InkProjectileRenderState;
import fr.hugman.mubble.splatoon.world.entity.projectile.InkProjectile;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

/**
 * Draws ink in flight: a blob in the color of its ink, the size of the entity, stretched along its path the faster it
 * goes.
 */
public class InkProjectileRenderer<T extends InkProjectile> extends EntityRenderer<T, InkProjectileRenderState> {
    private static final Identifier TEXTURE = Splatoon.id("textures/block/ink/normal.png");
    /**
     * How much longer a blob gets per block per tick of speed.
     */
    private static final float STRETCH_PER_SPEED = 0.35F;
    private static final float MAX_STRETCH = 3.0F;
    /**
     * How big the blob is compared to the entity.
     */
    private static final float SIZE = 0.8F;

    private final InkBulletModel model;

    public InkProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new InkBulletModel(context.bakeLayer(SplatoonModelLayers.INK_BULLET));
    }

    @Override
    public InkProjectileRenderState createRenderState() {
        return new InkProjectileRenderState();
    }

    @Override
    public void extractRenderState(T projectile, InkProjectileRenderState state, float partialTicks) {
        super.extractRenderState(projectile, state, partialTicks);
        state.pitch = projectile.getXRot(partialTicks);
        state.yaw = projectile.getYRot(partialTicks);
        state.speed = projectile.getDeltaMovement().length();
        state.color = ARGB.opaque(projectile.getInkColor());
    }

    @Override
    public void submit(InkProjectileRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0F, state.boundingBoxHeight / 2.0F, 0.0F);
        // the model's depth axis follows the path
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-state.pitch));

        float size = state.boundingBoxWidth * SIZE / (InkBulletModel.SIZE / 16.0F);
        float stretch = Mth.clamp(1.0F + (float) state.speed * STRETCH_PER_SPEED, 1.0F, MAX_STRETCH);
        float width = size / Mth.sqrt(stretch);
        poseStack.scale(width, width, size * stretch);

        submitNodeCollector.submitModel(
                this.model,
                state,
                poseStack,
                this.model.renderType(TEXTURE),
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.color,
                null,
                state.outlineColor,
                null
        );
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
