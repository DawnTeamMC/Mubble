package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class BlockDoor extends net.minecraft.block.BlockDoor
{
    public BlockDoor(String name, Block base_block)
    {
        super(Properties.from(base_block).hardnessAndResistance(3F));
        setRegistryName(Mubble.MOD_ID, name + "_door");
        MubbleBlocks.register(this, ItemGroup.REDSTONE);
    }
    
    public BlockDoor(Block base_block)
    {
        super(Properties.from(base_block).hardnessAndResistance(3F));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_door");
        MubbleBlocks.register(this, ItemGroup.REDSTONE);
    }
}