package hugman.mod.objects.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCake extends net.minecraft.block.BlockCake
{
	/* Extension for internal publicity */
    public BlockCake()
    {
        super(Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH));
    }
}