package hugman.mubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.world.MubbleDimensions;
import hugman.mubble.objects.block.PermafrostPortalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(FireBlock.class)
public class FireBlockMixin
{
	@Inject(method = "onBlockAdded", at = @At(value = "TAIL"), cancellable = true)
	private void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved, CallbackInfo ci)
	{
		FireBlock fireBlock = (FireBlock) (Object) this;
		if (oldState.getBlock() != state.getBlock())
		{
			if (world.dimension.getType() != DimensionType.OVERWORLD && world.dimension.getType() != MubbleDimensions.PERMAFROST || !((PermafrostPortalBlock) MubbleBlocks.PERMAFROST_PORTAL).trySpawnPortal(world, pos))
			{
				if (!state.canPlaceAt(world, pos))
				{
					world.removeBlock(pos, false);
				}
				else
				{
					world.getBlockTickScheduler().schedule(pos, fireBlock, fireBlock.getTickRate(world) + world.random.nextInt(10));
				}
			}
		}
	}
}
