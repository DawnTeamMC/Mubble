package hugman.mubble.objects.block;

import javax.annotation.Nullable;

import hugman.mubble.init.MubbleBlockStateProperties;
import hugman.mubble.objects.block_state.properties.VerticalSlabType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class VerticalSlabBlock extends Block implements IWaterLoggable
{
	public static final EnumProperty<VerticalSlabType> TYPE = MubbleBlockStateProperties.VERTICAL_SLAB_TYPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape NORTH_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape EAST_SHAPE = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape WEST_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
	
    public VerticalSlabBlock(Block.Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(TYPE, VerticalSlabType.NORTH).with(WATERLOGGED, Boolean.valueOf(false)));
    }
    
    @Override
    public boolean func_220074_n(BlockState state)
    {
    	return state.get(TYPE) != VerticalSlabType.DOUBLE;
    }
    
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return worldIn.getMaxLightLevel();
	}

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(TYPE, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		VerticalSlabType verticalslabtype = state.get(TYPE);
		switch(verticalslabtype)
		{
		case DOUBLE:
			return VoxelShapes.fullCube();
        case SOUTH:
        	return SOUTH_SHAPE;
        case EAST:
            return EAST_SHAPE;
        case WEST:
            return WEST_SHAPE;
        default:
        	return NORTH_SHAPE;
		}
	}
	
	@Override
	public boolean isSolid(BlockState state)
	{
		return state.get(TYPE) == VerticalSlabType.DOUBLE;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState iblockstate = context.getWorld().getBlockState(context.getPos());
        if (iblockstate.getBlock() == this)
        {
        	return iblockstate.with(TYPE, VerticalSlabType.DOUBLE).with(WATERLOGGED, Boolean.valueOf(false));
        }
        else
        {
        	IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        	BlockState iblockstate1 = this.getDefaultState().with(TYPE, VerticalSlabType.NORTH).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
        	Direction facing = context.getPlacementHorizontalFacing();
        	Direction face_hit = context.getFace();
        	
        	Vec3d vec3d = context.getHitVec();
            double hitX = vec3d.x - (double)context.getPos().getX();
            double hitZ = vec3d.z - (double)context.getPos().getZ();
        	if(facing == Direction.NORTH || facing == Direction.SOUTH)
        	{
        		if(face_hit == Direction.SOUTH) return iblockstate1.with(TYPE, VerticalSlabType.NORTH);
        		else if(face_hit == Direction.NORTH) return iblockstate1.with(TYPE, VerticalSlabType.SOUTH);
        		else if(hitZ > 0.5D) return iblockstate1.with(TYPE, VerticalSlabType.SOUTH);
        		else return iblockstate1.with(TYPE, VerticalSlabType.NORTH);
        	}
        	else if (facing == Direction.EAST || facing == Direction.WEST)
        	{
        		if(face_hit == Direction.WEST) return iblockstate1.with(TYPE, VerticalSlabType.EAST);
        		else if(face_hit == Direction.EAST) return iblockstate1.with(TYPE, VerticalSlabType.WEST);
        		else if(hitX > 0.5D) return iblockstate1.with(TYPE, VerticalSlabType.EAST);
        		else return iblockstate1.with(TYPE, VerticalSlabType.WEST);
        	}
        	else return iblockstate1;
        }
    }

	@Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext context)
	{
        ItemStack itemstack = context.getItem();
        VerticalSlabType slabtype = state.get(TYPE);
        if (slabtype != VerticalSlabType.DOUBLE && itemstack.getItem() == this.asItem())
        {
            if (context.replacingClickedOnBlock())
            {
        	    Vec3d vec3d = context.getHitVec();
        	    double hitX = vec3d.x - (double)context.getPos().getX();
        	    double hitZ = vec3d.z - (double)context.getPos().getZ();
        	    boolean flag1 = hitZ > 0.5D;
        	    boolean flag2 = hitX > 0.5D;
        	    Direction enumfacing = context.getFace();
        	    if (slabtype == VerticalSlabType.NORTH) return enumfacing == Direction.SOUTH || flag1 && enumfacing.getAxis().isHorizontal();
        	    if (slabtype == VerticalSlabType.SOUTH) return enumfacing == Direction.NORTH || !flag1 && enumfacing.getAxis().isHorizontal();
        	    if (slabtype == VerticalSlabType.EAST) return enumfacing == Direction.WEST || !flag2 && enumfacing.getAxis().isHorizontal();
        	    else return enumfacing == Direction.EAST || flag2 && enumfacing.getAxis().isHorizontal();
            }
            else
            {
            	return true;
            }
        }
        else
        {
        	return false;
        }
	}

	@Override
	public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state)
	{
        if (state.get(WATERLOGGED))
        {
            worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(false)), 3);
            return Fluids.WATER;
        }
        else
        {
            return Fluids.EMPTY;
        }
	}
	
	@Override
	public IFluidState getFluidState(BlockState state)
	{
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}

	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn)
	{
        return state.get(TYPE) != VerticalSlabType.DOUBLE && !state.get(WATERLOGGED) && fluidIn == Fluids.WATER;
	}

	@Override
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn)
	{
        if (state.get(TYPE) != VerticalSlabType.DOUBLE && !state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER)
        {
            if (!worldIn.isRemote())
            {
            	worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)), 3);
            	worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            }
            return true;
        }
        else
        {
            return false;
        }
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
        if (stateIn.get(WATERLOGGED))
        {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return stateIn;
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
	{
        switch(type)
        {
        	case WATER:
        		return worldIn.getFluidState(pos).isTagged(FluidTags.WATER);
        	case AIR:
        		return false;
        	default:
        		return false;
        }
	}
}
