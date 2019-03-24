package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.init.Blocks;

public class BlockTrapDoor extends net.minecraft.block.BlockTrapDoor
{
    public BlockTrapDoor(String name)
    {
        super(Properties.from(Blocks.OAK_TRAPDOOR));
        setRegistryName(Mubble.MOD_ID, name + "_trapdoor");
        MubbleBlocks.register(this);
    }
}