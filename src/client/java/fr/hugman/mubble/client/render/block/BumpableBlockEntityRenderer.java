package fr.hugman.mubble.client.render.block;

import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.client.render.block.state.BumpableBlockEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.MovingBlockRenderState;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public class BumpableBlockEntityRenderer implements BlockEntityRenderer<BumpableBlockEntity, BumpableBlockEntityRenderState> {
    public BumpableBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

	@Override
	public BumpableBlockEntityRenderState createRenderState() {
		return new BumpableBlockEntityRenderState();
	}

	@Override
	public void updateRenderState(BumpableBlockEntity blockEntity, BumpableBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
		BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
		state.bumpProgress = blockEntity.getBumpProgress(tickProgress);
		if(blockEntity.getBumpDirection() != null) {
			state.bumpVector = blockEntity.getBumpDirection().getVector();
		}

		state.movingState = null;

		var world = blockEntity.getWorld();
		if(world != null && state.isBumping()) {
			var pos = blockEntity.getPos();
			state.movingState = renderModel(pos, blockEntity.getCachedState(), world.getBiome(pos), world);
		}
	}

	@Override
	public void render(BumpableBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
		if (state.movingState != null) {
			matrices.push();
			this.applyTransformations(matrices, state.bumpProgress, state.bumpVector);
			queue.submitMovingBlock(matrices, state.movingState);
			matrices.pop();
		}
	}

    private void applyTransformations(MatrixStack matrices, float bumpProgress, @Nullable Vec3i vector) {
		var amplitude = 1;
        double i = -amplitude * Math.pow(bumpProgress, 2) + amplitude * bumpProgress;
        float scale = (float) i + 1;

		if(vector == null) {
			matrices.scale(scale, scale, scale);
			return;
		}

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

	private static MovingBlockRenderState renderModel(BlockPos pos, BlockState state, RegistryEntry<Biome> biome, World world) {
		MovingBlockRenderState movingBlockRenderState = new MovingBlockRenderState();
		movingBlockRenderState.fallingBlockPos = pos;
		movingBlockRenderState.entityBlockPos = pos;
		movingBlockRenderState.blockState = state;
		movingBlockRenderState.biome = biome;
		movingBlockRenderState.world = world;
		return movingBlockRenderState;
	}
}
