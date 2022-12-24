package fr.hugman.mubble.block;

import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class QuestionBlock extends Block implements UnderHittableBlock {
	public QuestionBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onHitFromUnder(BlockState state, World world, BlockPos pos, BlockHitResult hitResult) {
		if(!world.isClient()) {
			this.trigger(world, pos);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(!world.isClient()) {
			this.trigger(world, pos);
		}
		return ActionResult.success(world.isClient());
	}

	public void trigger(World world, BlockPos pos) {
		world.setBlockState(pos, SuperMarioContent.EMPTY_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
	}
}
