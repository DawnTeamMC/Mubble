package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class BlockFenceGate extends net.minecraft.block.BlockFenceGate
{
    public BlockFenceGate(String name, Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_fence_gate");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
    }
    
    public BlockFenceGate(Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_fence_gate");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
    }
}