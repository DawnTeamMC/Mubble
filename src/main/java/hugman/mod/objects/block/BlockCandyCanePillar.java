package hugman.mod.objects.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;

public class BlockCandyCanePillar extends BlockRotatedPillar
{
    public BlockCandyCanePillar(EnumDyeColor color)
    {
        super(color.getTranslationKey() + "_candy_cane_pillar", Properties.create(Material.CRAFTED_SNOW, color).hardnessAndResistance(0.2F), ItemGroup.DECORATIONS);
    }
}
