package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;

public class BlockStairs extends net.minecraft.block.BlockStairs implements IBucketPickupHandler, ILiquidContainer
{
    public BlockStairs(String name, Block base_block)
    {
        super(base_block.getDefaultState(), Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_stairs");
        MubbleBlocks.register(this);
    }
    
    public BlockStairs(Block base_block)
    {
        super(base_block.getDefaultState(), Properties.from(base_block));
        setRegistryName(base_block.getRegistryName() + "_stairs");
        MubbleBlocks.register(this);
    }
}
