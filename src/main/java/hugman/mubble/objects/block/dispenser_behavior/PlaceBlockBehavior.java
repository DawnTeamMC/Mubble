package hugman.mubble.objects.block.dispenser_behavior;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceBlockBehavior extends OptionalDispenseBehavior
{
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
	{
		this.successful = false;
		Item item = stack.getItem();
		World worldIn = source.getWorld();
		Direction direction = source.getBlockState().get(DispenserBlock.FACING);
		BlockPos blockPos = source.getBlockPos().offset(direction);
		BlockState blockState = worldIn.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if(item instanceof BlockItem)
		{
			BlockItem blockItem = (BlockItem)item;
			this.successful = blockItem.tryPlace(new DirectionalPlaceContext(source.getWorld(), blockPos, direction, stack, direction)) == ActionResultType.SUCCESS;
		}
		else if(item instanceof ToolItem)
		{
			if(item.canHarvestBlock(blockState) || blockState.getMaterial().isToolNotRequired())
			{
				if(block instanceof AirBlock || block instanceof FlowingFluidBlock)
				{
					this.successful = false;
				}
				else if(blockState.getBlockHardness(worldIn, blockPos) < 0.0f)
				{
					this.successful = false;
				}
				else
				{
					this.successful = true;
				}
			}
			if(this.successful)
			{
				worldIn.destroyBlock(blockPos, true);
				stack.attemptDamageItem(1, worldIn.getRandom(), null);
			}
		}
		return stack;
	}
}