package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class BlockFence extends net.minecraft.block.BlockFence
{
    public BlockFence(String name, Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_fence");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
    }
    
    public BlockFence(Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_fence");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
    }
}