package hugman.mubble.objects.event_handler;

import java.util.Map;

import com.google.common.collect.ImmutableMap.Builder;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class StripWoodHandler
{
	public static final Map<Block, Block> BLOCK_STRIPPING_MAP = (new Builder<Block, Block>())
			.put(MubbleBlocks.PALM_LOG, MubbleBlocks.STRIPPED_PALM_LOG)
			.put(MubbleBlocks.PALM_WOOD, MubbleBlocks.STRIPPED_PALM_WOOD)
			.put(MubbleBlocks.SCARLET_LOG, MubbleBlocks.STRIPPED_SCARLET_LOG)
			.put(MubbleBlocks.SCARLET_WOOD, MubbleBlocks.STRIPPED_SCARLET_WOOD)
			.put(MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.STRIPPED_CHERRY_OAK_LOG)
			.put(MubbleBlocks.CHERRY_OAK_WOOD, MubbleBlocks.STRIPPED_CHERRY_OAK_WOOD)
			.put(MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.STRIPPED_PRESS_GARDEN_LOG)
			.put(MubbleBlocks.PRESS_GARDEN_WOOD, MubbleBlocks.STRIPPED_PRESS_GARDEN_WOOD)
			.build();
	
	@SubscribeEvent
	public static void onRightClick(RightClickBlock event)
	{
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		Block block = BLOCK_STRIPPING_MAP.get(state.getBlock());
		PlayerEntity player = event.getPlayer();
		ItemStack stack = event.getItemStack();
		if(block != null && stack.getItem() instanceof AxeItem)
		{
	         world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
	         if(!world.isRemote)
	         {
	        	 world.setBlockState(pos, block.getDefaultState().with(LogBlock.AXIS, state.get(LogBlock.AXIS)), 11);
	        	 if(player != null)
	        	 {
	        		 stack.damageItem(1, player, (entity) ->
	        		 {
	        			 entity.sendBreakAnimation(event.getHand());
	        		 });
	        	 }
	         }
	         event.setCancellationResult(ActionResultType.SUCCESS);
	         event.setCanceled(true);
		}
		else
		{
	         event.setCancellationResult(ActionResultType.PASS);
		}
	}
}
