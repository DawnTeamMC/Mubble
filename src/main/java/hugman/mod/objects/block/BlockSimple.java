package hugman.mod.objects.block;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;

public class BlockSimple extends Block
{    
    public BlockSimple(String name, Properties properties)
    {
        super(properties);
        setRegistryName(Reference.MOD_ID, name);
        MubbleBlocks.register(this);
    }
}
