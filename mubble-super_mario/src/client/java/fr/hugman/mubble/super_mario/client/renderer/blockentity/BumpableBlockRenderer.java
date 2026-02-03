package fr.hugman.mubble.super_mario.client.renderer.blockentity;

import fr.hugman.mubble.super_mario.client.renderer.blockentity.state.BumpableBlockRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.world.level.block.entity.BumpableBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public class BumpableBlockRenderer implements BlockEntityRenderer<BumpableBlockEntity, BumpableBlockRenderState> {
	@Override
	public BumpableBlockRenderState createRenderState() {
		return new BumpableBlockRenderState();
	}

	@Override
	public void extractRenderState(BumpableBlockEntity blockEntity, BumpableBlockRenderState bumpableBlockRenderState, float f, Vec3 vec3, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, bumpableBlockRenderState, f, vec3, crumblingOverlay);
		bumpableBlockRenderState.bumping = blockEntity.isBumping();
		bumpableBlockRenderState.bumpTicks = blockEntity.getBumpTicks() + f;
		if(blockEntity.getBumpDirection() != null) {
				bumpableBlockRenderState.bumpVector = blockEntity.getBumpDirection().getUnitVec3i();
		}

		bumpableBlockRenderState.movingState = null;
		var world = blockEntity.getLevel();
		if(world != null) {
			var pos = blockEntity.getBlockPos();
			bumpableBlockRenderState.movingState = renderModel(pos, blockEntity.getBlockState(), world.getBiome(pos), world);
		}
	}

	@Override
	public void submit(BumpableBlockRenderState bumpableBlockRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		poseStack.pushPose();
		if(bumpableBlockRenderState.bumping) {
			applyTransformations(poseStack, bumpableBlockRenderState.bumpTicks, BumpableBlockEntity.BUMP_LENGTH, 0.25f, bumpableBlockRenderState.bumpVector);
		}
		submitNodeCollector.submitMovingBlock(poseStack, bumpableBlockRenderState.movingState);
		poseStack.popPose();
	}

    private void applyTransformations(
			PoseStack matrices,
			float ticks,
			float totalTicks,
			float amplitude,
			@Nullable Vec3i vector
	) {
		double i = amplitude * Math.sin((ticks / totalTicks) * Math.PI);
        float scale = (float) i + 1;

		if(vector == null) {
			matrices.translate(
				0.5 * i, 0.5 * i, 0.5 * i
			);
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

	private static MovingBlockRenderState renderModel(BlockPos pos, BlockState state, Holder<Biome> biome, Level level) {
		MovingBlockRenderState movingBlockRenderState = new MovingBlockRenderState();
		movingBlockRenderState.randomSeedPos = pos;
		movingBlockRenderState.blockPos = pos;
		movingBlockRenderState.blockState = state;
		movingBlockRenderState.biome = biome;
		movingBlockRenderState.level = level;
		return movingBlockRenderState;
	}
}
