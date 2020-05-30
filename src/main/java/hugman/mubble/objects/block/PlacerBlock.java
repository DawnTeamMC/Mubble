package hugman.mubble.objects.block;

import hugman.mubble.objects.block.block_entity.PlacerBlockEntity;
import hugman.mubble.objects.block.dispenser_behavior.PlaceBreakDispenserBehavior;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;

public class PlacerBlock extends DispenserBlock
{
	private static final DispenserBehavior PLACE_BEHAVIOR = new PlaceBreakDispenserBehavior();

	public PlacerBlock(Settings builder)
	{
		super(builder);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new PlacerBlockEntity();
	}

	@Override
	protected DispenserBehavior getBehaviorForItem(ItemStack stack)
	{
		return PLACE_BEHAVIOR;
	}
}
