package hugman.mubble.objects.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class TetrisGlassBlock extends FallingBlock
{
	public TetrisGlassBlock(Block.Settings builder)
	{
		super(builder);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState neighbor, Direction facing)
	{
		return neighbor.getBlock() == this || super.isSideInvisible(state, neighbor, facing);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView worldIn, BlockPos pos)
	{
		return 1.0F;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView reader, BlockPos pos)
	{
		return true;
	}
}
