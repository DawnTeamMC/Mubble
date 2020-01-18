package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleItems;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Material;
import net.minecraft.item.ItemConvertible;
import net.minecraft.sound.BlockSoundGroup;

public class CropsBlock extends CropBlock
{	
	/* Extension for internal publicity
	 * + Missing features */
    public CropsBlock()
    {
        super(FabricBlockSettings.of(Material.LEAVES).collidable(true).ticksRandomly().hardness(0f).sounds(BlockSoundGroup.CROP).nonOpaque().build());
    }
    
    @Override
    protected ItemConvertible getSeedsItem()
    {
    	if(this == MubbleBlocks.TOMATOES) return MubbleItems.TOMATO;
    	if(this == MubbleBlocks.SALAD) return MubbleItems.SALAD;
    	else return null;
	}
}
