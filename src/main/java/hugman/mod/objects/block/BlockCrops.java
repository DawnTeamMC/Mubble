package hugman.mod.objects.block;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;

public class BlockCrops extends net.minecraft.block.BlockCrops
{	
	/* Extension for internal publicity
	 * + Missing features */
    public BlockCrops()
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0f).sound(SoundType.PLANT));
    }
    
    protected IItemProvider getSeedFood()
    {
    	if(this == MubbleBlocks.TOMATOES) return MubbleItems.TOMATO;
    	if(this == MubbleBlocks.SALAD) return MubbleItems.SALAD;
    	else return null;
    }
    
    @Override
    protected IItemProvider getSeedsItem()
    {
    	return getSeedFood();
	}
    
    @Override
	protected IItemProvider getCropsItem()
	{
		return getSeedFood();
	}
}
