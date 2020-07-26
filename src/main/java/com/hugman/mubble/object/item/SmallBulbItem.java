package com.hugman.mubble.object.item;

import com.hugman.mubble.object.block.GarlandBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallBulbItem extends Item {
	public SmallBulbItem(Item.Settings builder) {
		super(builder);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World worldIn = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = worldIn.getBlockState(pos);
		if(state.getBlock() instanceof GarlandBlock) {
			if(!state.get(GarlandBlock.ILLUMINATED)) {
				if(!worldIn.isClient) {
					worldIn.setBlockState(pos, state.with(GarlandBlock.ILLUMINATED, true), 2);
					context.getStack().decrement(1);
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.FAIL;
	}
}
