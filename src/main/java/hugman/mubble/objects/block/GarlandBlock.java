package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class GarlandBlock extends Block
{
	public static final BooleanProperty ILLUMINATED = MubbleBlockStateProperties.ILLUMINATED;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(3.0D, 3.0D, 3.0D, 10.0D, 10.0D, 10.0D);
	protected static final VoxelShape SELECTION_SHAPE = Block.createCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);
	
	public GarlandBlock(Settings properties)
	{
		super(properties.lightLevel((state) -> {
			return state.get(ILLUMINATED) ? 10 : 0;
		}));
        this.setDefaultState(this.stateManager.getDefaultState().with(ILLUMINATED, false));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder)
	{
    	builder.add(ILLUMINATED);
	}
	
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context)
    {
    	return SELECTION_SHAPE;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public OffsetType getOffsetType()
    {
    	return OffsetType.XYZ;
    }
}