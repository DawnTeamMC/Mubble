package hugman.mubble.objects.block.dispenser_behavior;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PlaceBlockBehavior extends BlockPlacementDispenserBehavior
{
	protected ItemStack dispenseSilently(BlockPointer source, ItemStack stack)
	{
		this.success = false;
		Item item = stack.getItem();
		World worldIn = source.getWorld();
		Direction direction = source.getBlockState().get(DispenserBlock.FACING);
		BlockPos blockPos = source.getBlockPos().offset(direction);
		BlockState blockState = worldIn.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if(item instanceof BlockItem)
		{
			BlockItem blockItem = (BlockItem)item;
			this.success = blockItem.place(new AutomaticItemPlacementContext(source.getWorld(), blockPos, direction, stack, direction)) == ActionResult.SUCCESS;
		}
		else if(item instanceof ToolItem)
		{
			if(item.isEffectiveOn(blockState) || blockState.getMaterial().canBreakByHand())
			{
				if(block instanceof AirBlock || block instanceof FluidBlock)
				{
					this.success = false;
				}
				else if(blockState.getHardness(worldIn, blockPos) < 0.0f)
				{
					this.success = false;
				}
				else
				{
					this.success = true;
				}
			}
			if(this.success)
			{
				worldIn.removeBlock(blockPos, true);
				stack.damage(1, worldIn.getRandom(), null);
			}
		}
		return stack;
	}
}