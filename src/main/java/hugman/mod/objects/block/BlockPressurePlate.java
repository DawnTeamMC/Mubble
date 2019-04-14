package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class BlockPressurePlate extends net.minecraft.block.BlockPressurePlate
{    
    public BlockPressurePlate(String name, Block base_block)
    {
        super(BlockPressurePlate.Sensitivity.EVERYTHING, Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_pressure_plate");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
    }
    
    public BlockPressurePlate(Block base_block)
    {
        super(BlockPressurePlate.Sensitivity.EVERYTHING, Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, base_block.getRegistryName().getPath() + "_pressure_plate");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
    }
}