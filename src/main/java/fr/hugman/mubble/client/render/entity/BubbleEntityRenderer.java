package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.BubbleEntity;
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
import net.minecraft.util.math.Matrix4f;

/**
 * Renders {@link BubbleEntity} as a camera-facing (billboard) sprite.
 *
 * @author Copilot
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public class BubbleEntityRenderer extends EntityRenderer<BubbleEntity> {
	private static final Identifier TEXTURE = Mubble.id("textures/entity/bubble.png");
	/** Alpha value for the bubble sprite (slightly translucent). */
	private static final int SPRITE_ALPHA = 200;

	public BubbleEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(BubbleEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(BubbleEntity entity, float yaw, float tickDelta,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();

		float size = entity.getBubbleSize();

		// Scale to match the bubble's logical size
		matrices.scale(size * 2.0f, size * 2.0f, size * 2.0f);

		// Billboard: rotate to face the camera using the dispatcher's stored rotation
		matrices.multiply(this.dispatcher.getRotation());

		// Flip so the texture appears correctly (mirrors Minecraft billboard convention)
		matrices.scale(-1.0f, -1.0f, 1.0f);

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
				RenderLayer.getEntityTranslucentCull(TEXTURE));

		Matrix4f matrix = matrices.peek().getPositionMatrix();

		// Draw a unit quad centred on the entity origin
		int overlay = OverlayTexture.DEFAULT_UV;
		vertexConsumer.vertex(matrix, -0.5f, -0.5f, 0).color(255, 255, 255, SPRITE_ALPHA)
				.texture(0.0f, 1.0f).overlay(overlay).light(light).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix, 0.5f, -0.5f, 0).color(255, 255, 255, SPRITE_ALPHA)
				.texture(1.0f, 1.0f).overlay(overlay).light(light).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix, 0.5f, 0.5f, 0).color(255, 255, 255, SPRITE_ALPHA)
				.texture(1.0f, 0.0f).overlay(overlay).light(light).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix, -0.5f, 0.5f, 0).color(255, 255, 255, SPRITE_ALPHA)
				.texture(0.0f, 0.0f).overlay(overlay).light(light).normal(0, 1, 0).next();

		matrices.pop();

		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
