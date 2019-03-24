package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.init.Blocks;

public class BlockButtonWood extends net.minecraft.block.BlockButtonWood
{
    public BlockButtonWood(String name)
    {
        super(Properties.from(Blocks.OAK_BUTTON));
        setRegistryName(Mubble.MOD_ID, name + "_button");
        MubbleBlocks.register(this);
    }
}