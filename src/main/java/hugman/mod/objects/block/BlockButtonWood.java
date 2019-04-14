package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class BlockButtonWood extends net.minecraft.block.BlockButtonWood
{
    public BlockButtonWood(String name, Block base_block)
    {
        super(Properties.from(base_block).hardnessAndResistance(0.5F));
        setRegistryName(Mubble.MOD_ID, name + "_button");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
    
    public BlockButtonWood(Block base_block)
    {
        super(Properties.from(base_block).hardnessAndResistance(0.5F));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_button");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
}