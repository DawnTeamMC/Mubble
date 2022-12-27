package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BumpableBlock extends Block implements UnderHittableBlock {
	public BumpableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			BumpedBlockEntity.bump(world, pos);
		}
		return ActionResult.success(world.isClient());
	}

	@Override
	public void onHitFromUnder(BlockState state, World world, BlockPos pos, BlockHitResult hitResult) {
		if (!world.isClient()) {
			BumpedBlockEntity.bump(world, pos);
		}
	}

	/**
	 * Called when a block finishes being bumped.
	 * 
	 * @return the block state to convert to
	 */
	protected abstract BlockState onBumpCompleted(BumpedBlockEntity entity);

	public static BlockState onBumpCompleted(BlockState state, BumpedBlockEntity entity) {
		if (state.getBlock() instanceof BumpableBlock block) {
			return block.onBumpCompleted(entity);
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}
}
