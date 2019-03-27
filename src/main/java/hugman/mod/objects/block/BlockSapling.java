package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleTags;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.trees.OakTree;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockSapling extends net.minecraft.block.BlockSapling
{
    public BlockSapling(String name)
    {
        super(new OakTree(), Properties.from(Blocks.OAK_SAPLING));
        setRegistryName(Mubble.MOD_ID, name + "_sapling");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
        MubbleBlocks.registerWithoutItem(new BlockFlowerPot("potted_" + name + "_sapling", this));
    }
    
    @Override
    protected boolean isValidGround(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	Block block = state.getBlock();
        if(this == MubbleBlocks.PALM_SAPLING) return MubbleTags.Blocks.VALID_GROUND_PALM_SAPLING.contains(block);
        else return super.isValidGround(state, worldIn, pos);
    }
}