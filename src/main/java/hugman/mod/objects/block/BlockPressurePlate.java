package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.init.Blocks;

public class BlockPressurePlate extends net.minecraft.block.BlockPressurePlate
{
    public BlockPressurePlate(String name)
    {
        super(BlockPressurePlate.Sensitivity.EVERYTHING, Properties.from(Blocks.OAK_PRESSURE_PLATE));
        setRegistryName(Mubble.MOD_ID, name + "_pressure_plate");
        MubbleBlocks.register(this);
    }
}