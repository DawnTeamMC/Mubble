package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;

public class BlockWall extends net.minecraft.block.BlockWall implements IBucketPickupHandler, ILiquidContainer
{
    public BlockWall(String name, Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_wall");
        MubbleBlocks.register(this);
    }
    
    public BlockWall(Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(base_block.getRegistryName() + "_wall");
        MubbleBlocks.register(this);
    }
}
