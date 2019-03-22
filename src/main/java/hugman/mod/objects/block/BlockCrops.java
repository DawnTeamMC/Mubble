package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleBlocks;
import hugman.mod.init.elements.MubbleItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;

public class BlockCrops extends net.minecraft.block.BlockCrops
{	
    public BlockCrops(String name)
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0f).sound(SoundType.PLANT));
        setRegistryName(Mubble.MOD_ID, name);
        MubbleBlocks.BLOCKS.add(this);
    }
    
    protected IItemProvider getSeedFood()
    {
    	if(this == MubbleBlocks.TOMATO) return MubbleItems.TOMATO;
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
