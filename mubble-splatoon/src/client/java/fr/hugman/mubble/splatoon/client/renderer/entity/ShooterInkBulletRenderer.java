package fr.hugman.mubble.splatoon.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.splatoon.client.model.InkBulletModel;
import fr.hugman.mubble.splatoon.client.model.SplatoonModelLayers;
import fr.hugman.mubble.splatoon.client.renderer.entity.state.ShooterInkBulletRenderState;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBullet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class ShooterInkBulletRenderer extends EntityRenderer<ShooterInkBullet, ShooterInkBulletRenderState> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/block/light_blue_concrete.png");
    private static final RenderType LAYER = RenderTypes.entityTranslucent(TEXTURE);

    private final InkBulletModel model;
    private final float MIN_SQUISH = 0.45f;
    private final float MAX_SQUISH = 2.0f;
    private final float SPEED_SQUISH_SCALE = 0.5f;

    public ShooterInkBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new InkBulletModel(context.bakeLayer(SplatoonModelLayers.INK_BULLET));
    }

    @Override
    public ShooterInkBulletRenderState createRenderState() {
        return new ShooterInkBulletRenderState();
    }

    @Override
    public void submit(ShooterInkBulletRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.translate(0.0D, state.boundingBoxHeight / 2.0D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.pitch));

        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(state.ageInTicks * 0.15F) * 360.0F));

        var newScale = state.boundingBoxWidth / (InkBulletModel.SIZE / 16) - MAX_SQUISH * 2;
        poseStack.scale(newScale, newScale, newScale);

        float squish = (float) Math.max(Math.min(1 - state.speed * SPEED_SQUISH_SCALE, MAX_SQUISH), MIN_SQUISH);
        poseStack.scale(1/ (squish * squish), squish, squish);

        this.model.setupAnim(state);

        submitNodeCollector.submitModel(
                this.model,
                state,
                poseStack,
                this.model.renderType(TEXTURE),
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                null
        );
        poseStack.popPose();


        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
