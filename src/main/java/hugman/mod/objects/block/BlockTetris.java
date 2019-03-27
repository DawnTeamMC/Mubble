package hugman.mod.objects.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;

public class BlockTetris extends BlockFalling
{
    public BlockTetris(EnumDyeColor color)
    {
        super(color.getTranslationKey() + "_tetris_block", Properties.create(Material.ROCK, color).hardnessAndResistance(1.5F, 6.0F), ItemGroup.BUILDING_BLOCKS);
    }
}
