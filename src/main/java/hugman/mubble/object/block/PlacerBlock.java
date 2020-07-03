package hugman.mubble.object.block;

import hugman.mubble.init.data.MubbleStats;
import hugman.mubble.object.block.block_entity.PlacerBlockEntity;
import hugman.mubble.object.block.dispenser_behavior.PlaceBreakDispenserBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PlacerBlock extends DispenserBlock {
	private static final DispenserBehavior PLACE_BEHAVIOR = new PlaceBreakDispenserBehavior();

	public PlacerBlock(Settings builder) {
		super(builder);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new PlacerBlockEntity();
	}

	@Override
	protected DispenserBehavior getBehaviorForItem(ItemStack stack) {
		return PLACE_BEHAVIOR;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.isClient) {
			return ActionResult.SUCCESS;
		}
		else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			player.openHandledScreen((DispenserBlockEntity) blockEntity);
			player.incrementStat(MubbleStats.INSPECT_PLACER);
		}
		return ActionResult.CONSUME;
	}
}