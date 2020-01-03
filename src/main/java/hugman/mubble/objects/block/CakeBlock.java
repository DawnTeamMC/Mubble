package hugman.mubble.objects.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class CakeBlock extends net.minecraft.block.CakeBlock
{
	/* Extension for internal publicity */
    public CakeBlock()
    {
        super(FabricBlockSettings.of(Material.CAKE).hardness(0.5F).sounds(BlockSoundGroup.WOOL).build());
    }
}