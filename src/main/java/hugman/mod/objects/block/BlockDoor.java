package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemGroup;

public class BlockDoor extends net.minecraft.block.BlockDoor
{
    public BlockDoor(String name)
    {
        super(Properties.from(Blocks.OAK_DOOR));
        setRegistryName(Mubble.MOD_ID, name + "_door");
        MubbleBlocks.register(this, ItemGroup.REDSTONE);
    }
}