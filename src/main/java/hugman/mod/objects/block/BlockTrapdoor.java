package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class BlockTrapdoor extends net.minecraft.block.BlockTrapDoor
{
    public BlockTrapdoor(String name, Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_trapdoor");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
    }
    
    public BlockTrapdoor(Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_trapdoor");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
    }
}