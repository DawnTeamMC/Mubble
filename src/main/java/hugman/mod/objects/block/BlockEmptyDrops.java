package hugman.mod.objects.block;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;

public class BlockEmptyDrops extends net.minecraft.block.BlockEmptyDrops
{
    public BlockEmptyDrops(String name, Properties properties)
    {
        super(properties);
        setRegistryName(Reference.MOD_ID, name);
        MubbleBlocks.register(this);
    }
}
