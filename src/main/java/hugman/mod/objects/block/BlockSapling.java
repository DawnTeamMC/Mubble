package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.trees.OakTree;
import net.minecraft.init.Blocks;

public class BlockSapling extends net.minecraft.block.BlockSapling
{
    public BlockSapling(String name)
    {
        super(new OakTree(), Properties.from(Blocks.OAK_SAPLING));
        setRegistryName(Mubble.MOD_ID, name + "_sapling");
        MubbleBlocks.register(this);
        MubbleBlocks.registerWithoutItem(new BlockFlowerPot("potted_" + name + "_sapling", this));
    }
}