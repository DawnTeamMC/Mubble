package hugman.mod.objects.block;

import hugman.mod.Mubble;
import net.minecraft.block.Block;

public class SimpleBlock extends Block
{    
    public SimpleBlock(Properties properties, String name)
    {
        super(properties);
        setRegistryName(Mubble.ID, name);
    }
}
