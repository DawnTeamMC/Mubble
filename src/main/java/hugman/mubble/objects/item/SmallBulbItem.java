package hugman.mubble.objects.item;

import hugman.mubble.objects.block.GarlandBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallBulbItem extends Item
{
    public SmallBulbItem(Item.Properties builder)
    {
        super(builder);
    }
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
    	World worldIn = context.getWorld();
    	BlockPos pos = context.getPos();
    	BlockState state = worldIn.getBlockState(pos);
    	
    	if(state.getBlock() instanceof GarlandBlock)
    	{
			if(!state.get(GarlandBlock.ILLUMINATED))
			{
				if(!worldIn.isRemote)
				{
		            worldIn.setBlockState(pos, state.with(GarlandBlock.ILLUMINATED, true), 2);
		            context.getItem().shrink(1);
				}
	    		return ActionResultType.SUCCESS;
			}
    	}
		return ActionResultType.FAIL;
	}
}
