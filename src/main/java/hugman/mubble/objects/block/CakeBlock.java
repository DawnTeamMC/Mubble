package hugman.mubble.objects.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class CakeBlock extends net.minecraft.block.CakeBlock
{
	/* Extension for internal publicity */
    public CakeBlock()
    {
        super(Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH));
    }
}