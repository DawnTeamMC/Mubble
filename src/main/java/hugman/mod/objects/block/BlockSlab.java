package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.item.ItemGroup;

public class BlockSlab extends net.minecraft.block.BlockSlab implements IBucketPickupHandler, ILiquidContainer
{
    public BlockSlab(String name, Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_slab");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
    
    public BlockSlab(Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_slab");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
}
