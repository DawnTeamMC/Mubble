package hugman.mubble.objects.event_handler;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.objects.block.PermafrostPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class PortalCreationHandler
{
	@SubscribeEvent
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		World worldIn = event.getWorld().getWorld();
		BlockPos pos = event.getPos();
		BlockState state = event.getPlacedAgainst();
		Block block = event.getPlacedBlock().getBlock();
		
		if(block instanceof FireBlock)
		{
			if(worldIn.dimension.getType() != DimensionType.OVERWORLD && worldIn.getDimension().getType() != MubbleDimensions.PERMAFROST || !((PermafrostPortalBlock)MubbleBlocks.PERMAFROST_PORTAL).trySpawnPortal(worldIn, pos))
			{
				if (!state.isValidPosition(worldIn, pos))
				{
					worldIn.removeBlock(pos, false);
				}
				else
				{
					worldIn.getPendingBlockTicks().scheduleTick(pos, block, block.tickRate(worldIn) + worldIn.rand.nextInt(10));
				}
			}
		}
	}
}
