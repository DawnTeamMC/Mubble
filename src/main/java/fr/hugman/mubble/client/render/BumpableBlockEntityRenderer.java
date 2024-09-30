package fr.hugman.mubble.client.render;

import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public class BumpableBlockEntityRenderer implements BlockEntityRenderer<BumpableBlockEntity> {
    private final BlockRenderManager renderManager;

    public BumpableBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.renderManager = context.getRenderManager();
    }

    @Override
    public void render(BumpableBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();

        BlockState state = entity.getCachedState();
        boolean fancy = MinecraftClient.isFancyGraphicsOrBetter();

        if (!fancy && !entity.isBumping()) {
            return;
        }

        matrices.push();

        VertexConsumer buffer;
        if (entity.isBumping()) {
            buffer = vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(state));
            float bumpTicks = Math.min(entity.getBumpTicks() + tickDelta, BumpableBlockEntity.ANIMATION_TICKS);
            this.applyTransformations(matrices, bumpTicks, entity.getBumpDirection().getVector());
        } else {
            buffer = vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state));
        }

        // Get parameters
        BakedModel model = this.renderManager.getModel(state);

        Random random = Random.create();
        long seed = state.getRenderingSeed(pos);

        // Render
        this.renderManager.getModelRenderer().render(world, model, state, pos, matrices, buffer, false, random, seed, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }

    private void applyTransformations(MatrixStack matrices, float bumpTicks, Vec3i vector) {
        double i = -0.04 * Math.pow(bumpTicks, 2) + 0.2 * bumpTicks;
        float scale = (float) i + 1;

        double x2 = (1 - vector.getX()) * 0.5;
        double y2 = (1 - vector.getY()) * 0.5;
        double z2 = (1 - vector.getZ()) * 0.5;

        double x = vector.getX() * i + x2;
        double y = vector.getY() * i + y2;
        double z = vector.getZ() * i + z2;

        matrices.translate(x, y, z);
        matrices.scale(scale, scale, scale);
        matrices.translate(-x2, -y2, -z2);
    }
}
