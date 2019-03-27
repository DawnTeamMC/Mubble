package hugman.mod.objects.block;

import hugman.mod.Mubble;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlockFlowerPot extends net.minecraft.block.BlockFlowerPot
{
    public BlockFlowerPot(String name, Block block)
    {
        super(block, Properties.from(Blocks.FLOWER_POT));
        setRegistryName(Mubble.MOD_ID, name);
    }
}