package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.init.Blocks;

public class BlockFence extends net.minecraft.block.BlockFence
{
    public BlockFence(String name)
    {
        super(Properties.from(Blocks.OAK_FENCE));
        setRegistryName(Mubble.MOD_ID, name + "_fence");
        MubbleBlocks.register(this);
    }
}