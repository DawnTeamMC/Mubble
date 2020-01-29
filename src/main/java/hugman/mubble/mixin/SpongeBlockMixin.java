package hugman.mubble.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hugman.mubble.init.world.MubbleDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpongeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(SpongeBlock.class)
public class SpongeBlockMixin
{
	@Inject(method = "onBlockAdded", at = @At(value = "TAIL"), cancellable = true)
	private void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved, CallbackInfo ci)
	{
		if (world.dimension.getType() == MubbleDimensions.PERMAFROST)
		{
			world.setBlockState(pos, Blocks.WET_SPONGE.getDefaultState(), 3);
		}
	}
}
