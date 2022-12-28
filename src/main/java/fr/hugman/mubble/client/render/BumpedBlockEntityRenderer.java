package fr.hugman.mubble.client.render;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public class BumpedBlockEntityRenderer implements BlockEntityRenderer<BumpedBlockEntity> {
	private final BlockRenderManager renderManager;

	public BumpedBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
		this.renderManager = context.getRenderManager();
	}

	@Override
	public void render(BumpedBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockState state = entity.getBlockState();
		if (state.getRenderType() != BlockRenderType.MODEL) return;

		World world = entity.getWorld();
		BlockPos pos = entity.getPos();

		float bumpTicks = Math.min(entity.getBumpTicks() + tickDelta, BumpedBlockEntity.ANIMATION_TICKS);

		// Transformations
		matrices.push();
		this.applyTransformations(matrices, bumpTicks, entity.getBumpDirection());

		// Get parameters
		BakedModel model = this.renderManager.getModel(state);
		VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(state));

		Random random = Random.create();
		long seed = state.getRenderingSeed(pos);

		// Render
		this.renderManager.getModelRenderer().render(world, model, state, pos, matrices, buffer, false, random, seed, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}

	private void applyTransformations(MatrixStack matrices, float bumpTicks, Direction direction) {
		double i = -0.04 * Math.pow(bumpTicks, 2) + 0.2 * bumpTicks;
		float scale = (float) i + 1;

		double x2 = (1 - direction.getOffsetX()) * 0.5;
		double y2 = (1 - direction.getOffsetY()) * 0.5;
		double z2 = (1 - direction.getOffsetZ()) * 0.5;

		double x = direction.getOffsetX() * i + x2;
		double y = direction.getOffsetY() * i + y2;
		double z = direction.getOffsetZ() * i + z2;

		matrices.translate(x, y, z);
		matrices.scale(scale, scale, scale);
		matrices.translate(-x2, -y2, -z2);
	}
}
