package hugman.mod.objects.block;

import hugman.mod.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class SpringBlock extends DirectionalBlock implements IWaterLoggable
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape IRON_UP = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
	private static final VoxelShape PLATE_UP = Block.makeCuboidShape(1.0D, 6.0D, 1.0D, 15.0D, 9.0D, 15.0D);
	private static final VoxelShape SPRING_UP = VoxelShapes.or(IRON_UP, PLATE_UP);
	private static final VoxelShape IRON_DOWN = Block.makeCuboidShape(6.0D, 10.0D, 6.0D, 10.0D, 16.0D, 10.0D);
	private static final VoxelShape PLATE_DOWN = Block.makeCuboidShape(1.0D, 7.0D, 1.0D, 15.0D, 10.0D, 15.0D);
	private static final VoxelShape SPRING_DOWN = VoxelShapes.or(IRON_DOWN, PLATE_DOWN);
	private static final VoxelShape IRON_NORTH = Block.makeCuboidShape(6.0D, 6.0D, 10.0D, 10.0D, 10.0D, 16.0D);
	private static final VoxelShape PLATE_NORTH = Block.makeCuboidShape(1.0D, 1.0D, 7.0D, 15.0D, 15.0D, 10.0D);
	private static final VoxelShape SPRING_NORTH = VoxelShapes.or(IRON_NORTH, PLATE_NORTH);
	private static final VoxelShape IRON_SOUTH = Block.makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 6.0D);
	private static final VoxelShape PLATE_SOUTH = Block.makeCuboidShape(1.0D, 1.0D, 6.0D, 15.0D, 15.0D, 9.0D);
	private static final VoxelShape SPRING_SOUTH = VoxelShapes.or(IRON_SOUTH, PLATE_SOUTH);
	private static final VoxelShape IRON_EAST = Block.makeCuboidShape(0.0D, 6.0D, 6.0D, 6.0D, 10.0D, 10.0D);
	private static final VoxelShape PLATE_EAST = Block.makeCuboidShape(6.0D, 1.0D, 1.0D, 9.0D, 15.0D, 15.0D);
	private static final VoxelShape SPRING_EAST = VoxelShapes.or(IRON_EAST, PLATE_EAST);
	private static final VoxelShape IRON_WEST = Block.makeCuboidShape(10.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
	private static final VoxelShape PLATE_WEST = Block.makeCuboidShape(7.0D, 1.0D, 1.0D, 10.0D, 15.0D, 15.0D);
	private static final VoxelShape SPRING_WEST = VoxelShapes.or(IRON_WEST, PLATE_WEST);
	
    public SpringBlock(Block.Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(WATERLOGGED, Boolean.valueOf(false)));
    }
    
    @Override
    public PushReaction getPushReaction(BlockState state)
    {
    	return PushReaction.DESTROY;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder)
    {
    	builder.add(WATERLOGGED);
    	super.fillStateContainer(builder);
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
    	return true;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
		switch(state.get(FACING))
		{
        case UP:
            return SPRING_UP;
        case DOWN:
            return SPRING_DOWN;
        case NORTH:
            return SPRING_NORTH;
        case SOUTH:
            return SPRING_SOUTH;
        case EAST:
            return SPRING_EAST;
        case WEST:
            return SPRING_WEST;
        default:
        	return SPRING_UP;
		}
	}
    
    @Override
    public boolean isSolid(BlockState state)
    {
    	return false;
    }
    
	@Override
    public IFluidState getFluidState(BlockState state)
    {
    	return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        Direction direction = state.get(FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return Block.hasSolidSide(blockstate, worldIn, blockpos, direction);
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return facing.getOpposite() == stateIn.get(FACING) && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        IFluidState fluidState = context.getWorld().getFluidState(context.getPos());
        BlockState blockState = this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(fluidState.getFluid() == Fluids.WATER));
        return blockState.with(FACING, context.getFace());
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	Vec3d vec3d = entityIn.getMotion();
    	double keptXFactor = vec3d.x / 3;
    	double keptYFactor = vec3d.y / 3;
    	double keptZFactor = vec3d.z / 3;
    	switch (state.get(FACING))
    	{
		case UP:
			entityIn.setMotion(vec3d.x, keptYFactor + 1.5D, vec3d.z);
			break;
		case DOWN:
			entityIn.setMotion(vec3d.x, keptYFactor + -1.5D, vec3d.z);
			break;
		case NORTH:
			entityIn.setMotion(vec3d.x, keptYFactor + 0.2D, keptZFactor + -1.5D);
			break;
		case SOUTH:
			entityIn.setMotion(vec3d.x, keptYFactor + 0.2D, keptZFactor + 1.5D);
			break;
		case EAST:
			entityIn.setMotion(keptXFactor + 1.5D, keptYFactor + 0.2D, vec3d.z);
			break;
		case WEST:
			entityIn.setMotion(keptXFactor + -1.5D, keptYFactor + 0.2D, vec3d.z);
			break;
		default:
			break;
		}
    	entityIn.fallDistance = 0f;
    	worldIn.playSound((PlayerEntity)null, pos, MubbleSounds.BLOCK_SPRING_TRIGGER, SoundCategory.BLOCKS, 1f, 1f);
    }
    
    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
    	entityIn.fall(fallDistance, 0.0F);
    }
}