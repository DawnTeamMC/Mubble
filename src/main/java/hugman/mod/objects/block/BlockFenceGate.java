package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemGroup;

public class BlockFenceGate extends net.minecraft.block.BlockFenceGate
{
    public BlockFenceGate(String name)
    {
        super(Properties.from(Blocks.OAK_FENCE_GATE));
        setRegistryName(Mubble.MOD_ID, name + "_fence_gate");
        MubbleBlocks.register(this, ItemGroup.REDSTONE);
    }
}