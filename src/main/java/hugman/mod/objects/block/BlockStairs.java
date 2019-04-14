package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class BlockStairs extends net.minecraft.block.BlockStairs
{
    public BlockStairs(String name, Block base_block)
    {
        super(base_block.getDefaultState(), Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_stairs");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
    
    public BlockStairs(Block base_block)
    {
        super(base_block.getDefaultState(), Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_stairs");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
}
