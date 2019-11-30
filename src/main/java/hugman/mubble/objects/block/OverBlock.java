package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class OverBlock extends Block
{
	public static final BooleanProperty OVER = MubbleBlockStateProperties.OVER;
	
    public OverBlock(Block.Properties builder)
    {
        super(builder);
        this.setDefaultState(this.getDefaultState().with(OVER, false));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder)
    {
    	builder.add(OVER);
    }
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		if(isFaceAboveSolid(context.getWorld(), context.getPos()))
		{
			return this.getDefaultState().with(OVER, false);
		}
		else
		{
			return this.getDefaultState().with(OVER, true);
		}
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		if(!worldIn.isRemote)
		{
			if(state.get(OVER) && isFaceAboveSolid(worldIn, pos))
			{
				worldIn.setBlockState(pos, state.cycle(OVER), 2);
			}
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if(!worldIn.isRemote)
		{
			boolean flag = state.get(OVER);
			if(flag != !isFaceAboveSolid(worldIn, pos))
			{
				worldIn.setBlockState(pos, state.cycle(OVER), 2);
			}
		}
	}
	
	private boolean isFaceAboveSolid(World worldIn, BlockPos pos)
	{
		BlockPos blockpos = pos.offset(Direction.UP);
		BlockState blockstate = worldIn.getBlockState(blockpos);
		return blockstate.func_224755_d(worldIn, blockpos, Direction.DOWN);
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity)
	{
		if(state.getMaterial() == Material.EARTH)
		{
			if(state.get(OVER))
			{
				return SoundType.PLANT;
			}
			else
			{
				return SoundType.GROUND;
			}
		}
		return super.getSoundType(state, world, pos, entity);
	}
	
	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
	{
		if(state.getMaterial() == Material.EARTH)
		{
			return true;
		}
		return super.canSustainPlant(state, world, pos, facing, plantable);
	}
}