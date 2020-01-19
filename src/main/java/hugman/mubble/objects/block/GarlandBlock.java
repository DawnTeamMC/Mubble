package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class GarlandBlock extends Block
{
	public static final BooleanProperty ILLUMINATED = MubbleBlockStateProperties.ILLUMINATED;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(3.0D, 3.0D, 3.0D, 10.0D, 10.0D, 10.0D);
	protected static final VoxelShape SELECTION_SHAPE = Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);
	
	public GarlandBlock(Properties properties)
	{
		super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(ILLUMINATED, false));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
    	builder.add(ILLUMINATED);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public int getLightValue(BlockState state)
	{
		if(state.get(ILLUMINATED))
		{
			return super.getLightValue(state);
		}
		else
		{
			return 0;
		}
	}
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	return SELECTION_SHAPE;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	return SHAPE;
    }
    
	@Override
	public boolean canSuffocate(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public Vec3d getOffset(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
        long i = MathHelper.getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
        return new Vec3d(((double)((float)(i & 15L) / 15.0F) - 0.5D) * 0.1D, ((double)((float)(i & 15L) / 15.0F) - 0.5D) * 0.1D, ((double)((float)(i & 15L) / 15.0F) - 0.5D) * 0.1D);
	}
}