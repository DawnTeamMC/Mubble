package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.entity.projectile.ShooterInkBulletEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ShooterInkBulletRenderer extends EntityRenderer<ShooterInkBulletEntity> {
    public static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/projectiles/arrow.png");

    public ShooterInkBulletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(ShooterInkBulletEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ShooterInkBulletEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));
        boolean j = false;
        float h = 0.0f;
        float k = 0.5f;
        float l = 0.0f;
        float m = 0.15625f;
        float n = 0.0f;
        float o = 0.15625f;
        float p = 0.15625f;
        float q = 0.3125f;
        float scale = 0.05625f;
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0f));
        matrices.scale(scale, scale, scale);
        matrices.translate(-4.0f, 0.0f, 0.0f);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(this.getTexture(entity)));
        MatrixStack.Entry entry = matrices.peek();
        this.vertex(entry, vertexConsumer, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, light);
        this.vertex(entry, vertexConsumer, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, light);
        this.vertex(entry, vertexConsumer, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, light);
        this.vertex(entry, vertexConsumer, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, light);
        this.vertex(entry, vertexConsumer, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, light);
        this.vertex(entry, vertexConsumer, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, light);
        this.vertex(entry, vertexConsumer, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, light);
        this.vertex(entry, vertexConsumer, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, light);
        for (int u = 0; u < 4; ++u) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
            this.vertex(entry, vertexConsumer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, light);
            this.vertex(entry, vertexConsumer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, light);
            this.vertex(entry, vertexConsumer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, light);
            this.vertex(entry, vertexConsumer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, light);
        }
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int light) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z)
                .color(Colors.WHITE)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(matrix, (float)normalX, (float)normalY, (float)normalZ);
    }
}