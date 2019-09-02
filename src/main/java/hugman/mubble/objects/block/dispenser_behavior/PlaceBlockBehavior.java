package hugman.mubble.objects.block.dispenser_behavior;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class PlaceBlockBehavior extends OptionalDispenseBehavior
{
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
	{
		this.successful = false;
		Item item = stack.getItem();
		if (item instanceof BlockItem)
		{
			Direction direction = source.getBlockState().get(DispenserBlock.FACING);
			BlockPos blockpos = source.getBlockPos().offset(direction);
			this.successful = ((BlockItem)item).tryPlace(new DirectionalPlaceContext(source.getWorld(), blockpos, direction, stack, direction)) == ActionResultType.SUCCESS;
		}
		return stack;
	}
}