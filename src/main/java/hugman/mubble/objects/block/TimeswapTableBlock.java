package hugman.mubble.objects.block;

import com.sun.istack.internal.Nullable;
import hugman.mubble.Mubble;
import hugman.mubble.init.data.MubbleStats;
import hugman.mubble.objects.screen.screen_handler.TimeswapTableScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TimeswapTableBlock extends Block {
	private static final TranslatableText CONTAINER_NAME = new TranslatableText("container." + Mubble.MOD_ID + ".timeswap_table");

	public TimeswapTableBlock(Block.Settings builder) {
		super(builder);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.isClient) {
			return ActionResult.SUCCESS;
		}
		else {
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
			player.incrementStat(MubbleStats.INTERACT_WITH_TIMESWAP_TABLE);
			return ActionResult.CONSUME;
		}
	}

	@Nullable
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
			return new TimeswapTableScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos));
		}, CONTAINER_NAME);
	}

}