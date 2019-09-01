package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SaplingBlock extends net.minecraft.block.SaplingBlock
{
	/* Extension for internal publicity
	 * + Missing features */
    public SaplingBlock(Tree tree)
    {
        super(tree, Properties.from(Blocks.OAK_SAPLING));
    }
    
    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	Block block = state.getBlock();
        if(this == MubbleBlocks.PALM_SAPLING) return MubbleTags.Blocks.VALID_GROUND_PALM_SAPLING.contains(block);
        else return super.isValidGround(state, worldIn, pos);
    }
}