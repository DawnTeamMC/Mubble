package hugman.mod.objects.block;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleTags;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.trees.AbstractTree;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockSapling extends net.minecraft.block.BlockSapling
{
	/* Extension for internal publicity
	 * + Missing features */
    public BlockSapling(AbstractTree tree)
    {
        super(tree, Properties.from(Blocks.OAK_SAPLING));
    }
    
    @Override
    protected boolean isValidGround(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	Block block = state.getBlock();
        if(this == MubbleBlocks.PALM_SAPLING) return MubbleTags.Blocks.VALID_GROUND_PALM_SAPLING.contains(block);
        else return super.isValidGround(state, worldIn, pos);
    }
}