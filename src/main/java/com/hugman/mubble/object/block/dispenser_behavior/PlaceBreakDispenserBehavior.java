package com.hugman.mubble.object.block.dispenser_behavior;

import net.minecraft.block.*;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PlaceBreakDispenserBehavior extends BlockPlacementDispenserBehavior {
	protected ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
		this.setSuccess(false);
		Item item = stack.getItem();
		World world = source.getWorld();
		Direction direction = source.getBlockState().get(DispenserBlock.FACING);
		BlockPos pos = source.getBlockPos().offset(direction);
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if(item instanceof BlockItem) {
			BlockItem blockItem = (BlockItem) item;
			this.setSuccess(blockItem.place(new AutomaticItemPlacementContext(source.getWorld(), pos, direction, stack, direction)) == ActionResult.SUCCESS);
		}
		else if(item instanceof ToolItem) {
			if(item.isEffectiveOn(state) || state.getHardness(world, pos) <= 0.6f) {
				if(block instanceof AirBlock || block instanceof FluidBlock) {
					this.setSuccess(false);
				}
				else {
					this.setSuccess(!(state.getHardness(world, pos) < 0.0f));
				}
			}
			if(this.isSuccess()) {
				world.breakBlock(pos, true);
				stack.damage(1, world.getRandom(), null);
			}
		}
		return stack;
	}
}