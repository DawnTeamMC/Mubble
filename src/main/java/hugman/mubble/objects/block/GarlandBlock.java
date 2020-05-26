package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class GarlandBlock extends Block
{
	public static final BooleanProperty ILLUMINATED = MubbleBlockStateProperties.ILLUMINATED;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(3.0D, 3.0D, 3.0D, 10.0D, 10.0D, 10.0D);
	protected static final VoxelShape SELECTION_SHAPE = Block.createCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);
	
	public GarlandBlock(Settings properties)
	{
		super(properties);
        this.setDefaultState(this.stateManager.getDefaultState().with(ILLUMINATED, false));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder)
	{
    	builder.add(ILLUMINATED);
	}
	
	@Override
	public int getLuminance(BlockState state)
	{
		if(state.get(ILLUMINATED))
		{
			return super.getLuminance(state);
		}
		else
		{
			return 0;
		}
	}
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context)
    {
    	return SELECTION_SHAPE;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context)
    {
    	return SHAPE;
    }
	
	@Override
	public boolean canSuffocate(BlockState state, BlockView worldIn, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public Vec3d getOffsetPos(BlockState state, BlockView worldIn, BlockPos pos)
	{
        long i = MathHelper.hashCode(pos.getX(), pos.getY(), pos.getZ());
        return new Vec3d(((double)((float)(i & 15L) / 15.0F) - 0.5D) * 0.1D, ((double)((float)(i & 15L) / 15.0F) - 0.5D) * 0.1D, ((double)((float)(i & 15L) / 15.0F) - 0.5D) * 0.1D);
	}
}