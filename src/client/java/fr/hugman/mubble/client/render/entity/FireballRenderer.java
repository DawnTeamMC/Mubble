package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.entity.model.FireballEntityModel;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.client.render.entity.state.FireballRenderState;
import fr.hugman.mubble.entity.FireballEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class FireballRenderer extends EntityRenderer<FireballEntity, FireballRenderState> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/block/orange_concrete.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);

    private final FireballEntityModel model;
    private final float MIN_SQUISH = 0.45f;
    private final float MAX_SQUISH = 2.0f;
    private final float SPEED_SQUISH_SCALE = 0.5f;

    public FireballRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new FireballEntityModel(ctx.getPart(MubbleModelLayers.FIREBALL));
    }

    @Override
    public FireballRenderState createRenderState() {
        return new FireballRenderState();
    }

    @Override
    public void render(FireballRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.translate(0.0D, state.height / 2.0D, 0.0D);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(state.age * 0.15F) * 360.0F));

        var newScale = (state.width * 2) / (FireballEntityModel.SIZE / 16) - MAX_SQUISH * 2;
        matrices.scale(newScale, newScale, newScale);

        float squish = (float) Math.max(Math.min(1 - state.speed * SPEED_SQUISH_SCALE, MAX_SQUISH), MIN_SQUISH);
        matrices.scale(1 / squish, squish, squish);

        this.model.setAngles(state);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();

        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    public void updateRenderState(FireballEntity bullet, FireballRenderState state, float f) {
        super.updateRenderState(bullet, state, f);
        state.pitch = bullet.getLerpedPitch(f);
        state.yaw = bullet.getLerpedYaw(f);
        state.speed = bullet.getSpeed();
    }
}