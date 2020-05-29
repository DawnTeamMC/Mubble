package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class OverBlock extends Block
{
	public static final BooleanProperty OVER = MubbleBlockStateProperties.OVER;

	public OverBlock(Block.Settings builder)
	{
		super(builder);
		this.setDefaultState(this.getDefaultState().with(OVER, false));
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder)
	{
		builder.add(OVER);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		if (isFaceAboveSolid(context.getWorld(), context.getBlockPos()))
		{
			return this.getDefaultState().with(OVER, false);
		}
		else
		{
			return this.getDefaultState().with(OVER, true);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if (!world.isClient)
		{
			if (state.get(OVER) && isFaceAboveSolid(world, pos))
			{
				world.setBlockState(pos, state.cycle(OVER), 2);
			}
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if (!worldIn.isClient)
		{
			boolean flag = state.get(OVER);
			if (flag != !isFaceAboveSolid(worldIn, pos))
			{
				worldIn.setBlockState(pos, state.cycle(OVER), 2);
			}
		}
	}

	private boolean isFaceAboveSolid(World worldIn, BlockPos pos)
	{
		BlockPos blockpos = pos.offset(Direction.UP);
		BlockState blockstate = worldIn.getBlockState(blockpos);
		return blockstate.isSideSolidFullSquare(worldIn, blockpos, Direction.DOWN);
	}

	@Override
	public BlockSoundGroup getSoundGroup(BlockState state)
	{
		if (state.getMaterial() == Material.SOIL)
		{
			if (state.get(OVER))
			{
				return BlockSoundGroup.GRASS;
			}
			else
			{
				return BlockSoundGroup.STONE;
			}
		}
		return super.getSoundGroup(state);
	}
}