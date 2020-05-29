package hugman.mubble.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class MushroomBlock extends MushroomPlantBlock
{
	public MushroomBlock(Block.Settings builder)
	{
		super(builder);
	}

	@Override
	public boolean isFertilizable(BlockView worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return false;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return;
	}

	@Override
	public boolean canGrow(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return false;
	}

	@Override
	public boolean trySpawningBigMushroom(ServerWorld worldIn, BlockPos pos, BlockState state, Random rand)
	{
		return false;
	}
}
