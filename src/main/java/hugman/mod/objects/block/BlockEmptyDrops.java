package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.item.ItemGroup;

public class BlockEmptyDrops extends net.minecraft.block.BlockEmptyDrops
{
    public BlockEmptyDrops(String name, Properties properties, ItemGroup group)
    {
        super(properties);
        setRegistryName(Mubble.MOD_ID, name);
        MubbleBlocks.register(this, group);
    }
}
