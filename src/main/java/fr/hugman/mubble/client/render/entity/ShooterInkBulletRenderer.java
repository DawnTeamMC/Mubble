package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.entity.model.InkBulletEntityModel;
import fr.hugman.mubble.client.render.entity.model.MubbleEntityModelLayers;
import fr.hugman.mubble.entity.projectile.ShooterInkBulletEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class ShooterInkBulletRenderer extends EntityRenderer<ShooterInkBulletEntity> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/block/light_blue_concrete.png");
    private final InkBulletEntityModel<ShooterInkBulletEntity> model;
    private final float MIN_SQUISH = 0.45f;
    private final float MAX_SQUISH = 2.0f;
    private final float SPEED_SQUISH_SCALE = 0.5f;

    public ShooterInkBulletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new InkBulletEntityModel<>(ctx.getPart(MubbleEntityModelLayers.INK_BULLET));
    }

    @Override
    public Identifier getTexture(ShooterInkBulletEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ShooterInkBulletEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.translate(0.0D, entity.getHeight() / 2.0D, 0.0D);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));
        //TODO: roll with time on X axis

        var newScale = entity.getWidth() / (InkBulletEntityModel.SIZE / 16) - MAX_SQUISH * 2;
        matrices.scale(newScale, newScale, newScale);

        float squish = (float) Math.max(Math.min(1 - entity.getSpeed() * SPEED_SQUISH_SCALE, MAX_SQUISH), MIN_SQUISH);
        matrices.scale(1 / (squish * squish), squish, squish);

        this.model.setAngles(entity, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
