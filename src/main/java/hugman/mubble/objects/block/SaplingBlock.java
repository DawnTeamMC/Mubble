package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class SaplingBlock extends net.minecraft.block.SaplingBlock
{
	/* Extension for internal publicity
	 * + Missing features */
	public SaplingBlock(SaplingGenerator tree)
	{
		super(tree, Settings.copy(Blocks.OAK_SAPLING));
	}

	@Override
	protected boolean canPlantOnTop(BlockState state, BlockView worldIn, BlockPos pos)
	{
		Block block = state.getBlock();
        if (this == MubbleBlocks.PALM_SAPLING)
        {
            return MubbleTags.Blocks.PALM_SAPLING_VALID_GROUND.contains(block);
        }
        else return super.canPlantOnTop(state, worldIn, pos);
	}
}