package hugman.mod.objects.block;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

public class BlockCrops extends net.minecraft.block.BlockCrops
{
	Item seed, crops;
	
    public BlockCrops(String name, Item seed)
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0f).sound(SoundType.PLANT));
        setRegistryName(Reference.MOD_ID, name);
        MubbleBlocks.register(this);
        this.seed = seed;
        this.crops = seed;
    }
	
    public BlockCrops(String name, Item seed, Item crops)
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0f).sound(SoundType.PLANT));
        setRegistryName(Reference.MOD_ID, name);
        MubbleBlocks.register(this);
        this.seed = seed;
        this.crops = seed;
    }
    
    protected IItemProvider getSeedsItem()
    {
    	return seed;
	}

	protected IItemProvider getCropsItem()
	{
		return crops;
	}
}
