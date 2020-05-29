package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class SpringBlock extends DirectionalBlock implements Waterloggable
{
	public static final DirectionProperty FACING = Properties.FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	private static final VoxelShape IRON_UP = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
	private static final VoxelShape PLATE_UP = Block.createCuboidShape(1.0D, 6.0D, 1.0D, 15.0D, 9.0D, 15.0D);
	private static final VoxelShape IRON_DOWN = Block.createCuboidShape(6.0D, 10.0D, 6.0D, 10.0D, 16.0D, 10.0D);
	private static final VoxelShape PLATE_DOWN = Block.createCuboidShape(1.0D, 7.0D, 1.0D, 15.0D, 10.0D, 15.0D);
	private static final VoxelShape IRON_NORTH = Block.createCuboidShape(6.0D, 6.0D, 10.0D, 10.0D, 10.0D, 16.0D);
	private static final VoxelShape PLATE_NORTH = Block.createCuboidShape(1.0D, 1.0D, 7.0D, 15.0D, 15.0D, 10.0D);
	private static final VoxelShape IRON_SOUTH = Block.createCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 6.0D);
	private static final VoxelShape PLATE_SOUTH = Block.createCuboidShape(1.0D, 1.0D, 6.0D, 15.0D, 15.0D, 9.0D);
	private static final VoxelShape IRON_EAST = Block.createCuboidShape(0.0D, 6.0D, 6.0D, 6.0D, 10.0D, 10.0D);
	private static final VoxelShape PLATE_EAST = Block.createCuboidShape(6.0D, 1.0D, 1.0D, 9.0D, 15.0D, 15.0D);
	private static final VoxelShape IRON_WEST = Block.createCuboidShape(10.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
	private static final VoxelShape PLATE_WEST = Block.createCuboidShape(7.0D, 1.0D, 1.0D, 10.0D, 15.0D, 15.0D);
	private static final VoxelShape SPRING_UP = VoxelShapes.union(IRON_UP, PLATE_UP);
	private static final VoxelShape SPRING_DOWN = VoxelShapes.union(IRON_DOWN, PLATE_DOWN);
	private static final VoxelShape SPRING_NORTH = VoxelShapes.union(IRON_NORTH, PLATE_NORTH);
	private static final VoxelShape SPRING_SOUTH = VoxelShapes.union(IRON_SOUTH, PLATE_SOUTH);
	private static final VoxelShape SPRING_EAST = VoxelShapes.union(IRON_EAST, PLATE_EAST);
	private static final VoxelShape SPRING_WEST = VoxelShapes.union(IRON_WEST, PLATE_WEST);
	private static final VoxelShape COL_SPRING_UP = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 9.0D, 15.0D);
	private static final VoxelShape COL_SPRING_DOWN = Block.createCuboidShape(1.0D, 7.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	private static final VoxelShape COL_SPRING_NORTH = Block.createCuboidShape(1.0D, 1.0D, 7.0D, 15.0D, 15.0D, 16.0D);
	private static final VoxelShape COL_SPRING_SOUTH = Block.createCuboidShape(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 9.0D);
	private static final VoxelShape COL_SPRING_EAST = Block.createCuboidShape(0.0D, 1.0D, 1.0D, 9.0D, 15.0D, 15.0D);
	private static final VoxelShape COL_SPRING_WEST = Block.createCuboidShape(7.0D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D);

	public SpringBlock(Block.Settings builder)
	{
		super(builder);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP).with(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state)
	{
		return PistonBehavior.DESTROY;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder)
	{
		builder.add(WATERLOGGED);
		super.appendProperties(builder);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView reader, BlockPos pos)
	{
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context)
	{
		switch (state.get(FACING))
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
	public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context)
	{
		switch (state.get(FACING))
		{
			case UP:
				return COL_SPRING_UP;
			case DOWN:
				return COL_SPRING_DOWN;
			case NORTH:
				return COL_SPRING_NORTH;
			case SOUTH:
				return COL_SPRING_SOUTH;
			case EAST:
				return COL_SPRING_EAST;
			case WEST:
				return COL_SPRING_WEST;
			default:
				return COL_SPRING_UP;
		}
	}

	@Override
	public FluidState getFluidState(BlockState state)
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos)
	{
		Direction direction = state.get(FACING);
		BlockPos blockpos = pos.offset(direction.getOpposite());
		BlockState blockstate = worldIn.getBlockState(blockpos);
		return Block.isSideSolidFullSquare(blockstate, worldIn, blockpos, direction);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		return facing.getOpposite() == stateIn.get(FACING) && !stateIn.canPlaceAt(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(context.getWorld().getFluidState(context.getBlockPos()).getFluid() == Fluids.WATER)).with(FACING, context.getPlayerFacing());
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		Vec3d vec3d = entityIn.getVelocity();
		double keptXFactor = vec3d.x / 3;
		double keptYFactor = vec3d.y / 3;
		double keptZFactor = vec3d.z / 3;
		switch (state.get(FACING))
		{
			case UP:
				entityIn.setVelocity(vec3d.x, keptYFactor + 1.5D, vec3d.z);
				break;
			case DOWN:
				entityIn.setVelocity(vec3d.x, keptYFactor + -1.5D, vec3d.z);
				break;
			case NORTH:
				entityIn.setVelocity(vec3d.x, keptYFactor + 0.2D, keptZFactor + -1.5D);
				break;
			case SOUTH:
				entityIn.setVelocity(vec3d.x, keptYFactor + 0.2D, keptZFactor + 1.5D);
				break;
			case EAST:
				entityIn.setVelocity(keptXFactor + 1.5D, keptYFactor + 0.2D, vec3d.z);
				break;
			case WEST:
				entityIn.setVelocity(keptXFactor + -1.5D, keptYFactor + 0.2D, vec3d.z);
				break;
			default:
				break;
		}
		entityIn.fallDistance = 0f;
		worldIn.playSound((PlayerEntity) null, pos, MubbleSounds.BLOCK_SPRING_TRIGGER, SoundCategory.BLOCKS, 1f, 1f);
	}

	@Override
	public void onLandedUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		entityIn.handleFallDamage(fallDistance, 0.0F);
	}
}