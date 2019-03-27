package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.item.ItemGroup;

public class BlockFalling extends net.minecraft.block.BlockFalling
{
    public BlockFalling(String name, Properties properties, ItemGroup group)
    {
        super(properties);
        setRegistryName(Mubble.MOD_ID, name);
        MubbleBlocks.register(this, group);
    }
}
