package hugman.mubble.objects.event_handler;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.objects.block.PermafrostPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SpongeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class PermafrostEventsHandler
{
	@SubscribeEvent
	public static void createPortalOnBlockPlace(BlockEvent.EntityPlaceEvent event)
	{
		World world = event.getWorld().getWorld();
		BlockPos pos = event.getPos();
		BlockState state = event.getPlacedAgainst();
		Block placedBlock = event.getPlacedBlock().getBlock();
		
		if(placedBlock instanceof FireBlock)
		{
			if(world.dimension.getType() != DimensionType.OVERWORLD && world.dimension.getType() != MubbleDimensions.PERMAFROST || !((PermafrostPortalBlock)MubbleBlocks.PERMAFROST_PORTAL).trySpawnPortal(world, pos))
			{
				if(!state.isValidPosition(world, pos))
				{
					world.removeBlock(pos, false);
				}
				else
				{
					world.getPendingBlockTicks().scheduleTick(pos, placedBlock, placedBlock.tickRate(world) + world.rand.nextInt(10));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void wetSpongeOnPlace(BlockEvent.EntityPlaceEvent event)
	{
		World world = event.getWorld().getWorld();
		BlockPos pos = event.getPos();
		Block block = event.getPlacedBlock().getBlock();
		if(world.dimension.getType() == MubbleDimensions.PERMAFROST && block instanceof SpongeBlock)
		{
			world.setBlockState(pos, Blocks.WET_SPONGE.getDefaultState(), 3);
		}
	}
}
