package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;

public class CropsBlock extends net.minecraft.block.CropsBlock
{	
	/* Extension for internal publicity
	 * + Missing features */
    public CropsBlock()
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0f).sound(SoundType.CROP));
    }
    
    @Override
    protected IItemProvider getSeedsItem()
    {
    	if(this == MubbleBlocks.TOMATOES) return MubbleItems.TOMATO;
    	if(this == MubbleBlocks.SALAD) return MubbleItems.SALAD;
    	else return null;
	}
}
