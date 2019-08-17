package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.MubbleBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		if(context.getWorld().getBlockState(context.getPos().up()).isSolid())
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
			if(state.get(OVER) && worldIn.getBlockState(pos.up()).isSolid())
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
			if(flag != !worldIn.getBlockState(pos.up()).isSolid())
			{
				worldIn.setBlockState(pos, state.cycle(OVER), 2);
			}
		}
	}
}