package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SpringBlock extends WallMountedBlock implements Waterloggable {
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

	public SpringBlock(Block.Settings builder) {
		super(builder);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(FACE, WallMountLocation.WALL).with(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(FACING, FACE, WATERLOGGED);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView reader, BlockPos pos) {
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
		switch(state.get(FACE)) {
			case FLOOR:
				return SPRING_UP;
			case WALL:
				switch(state.get(FACING)) {
					case NORTH:
					default:
						return SPRING_NORTH;
					case SOUTH:
						return SPRING_SOUTH;
					case EAST:
						return SPRING_EAST;
					case WEST:
						return SPRING_WEST;
				}
			case CEILING:
			default:
				return SPRING_DOWN;
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
		switch(state.get(FACE)) {
			case FLOOR:
				return COL_SPRING_UP;
			case WALL:
				switch(state.get(FACING)) {
					case NORTH:
					default:
						return COL_SPRING_NORTH;
					case SOUTH:
						return COL_SPRING_SOUTH;
					case EAST:
						return COL_SPRING_EAST;
					case WEST:
						return COL_SPRING_WEST;
				}
			case CEILING:
			default:
				return COL_SPRING_DOWN;
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockState baseState = super.getPlacementState(context);
		FluidState fluidState = context.getWorld().getFluidState(context.getBlockPos());
		if(baseState != null) {
			return baseState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
		}
		else {
			return null;
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entityIn) {
		Vec3d vec3d = entityIn.getVelocity();
		double keptXFactor = vec3d.x / 3;
		double keptYFactor = vec3d.y / 3;
		double keptZFactor = vec3d.z / 3;
		switch(state.get(FACE)) {
			case FLOOR:
				entityIn.setVelocity(vec3d.x, keptYFactor + 1.5D, vec3d.z);
				break;
			case WALL:
				switch(state.get(FACING)) {
					case NORTH:
					default:
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
				}
				break;
			case CEILING:
			default:
				entityIn.setVelocity(vec3d.x, keptYFactor + -1.5D, vec3d.z);
				break;
		}
		entityIn.fallDistance = 0f;
		if(!world.isClient) {
			world.playSound(null, pos, MubbleSounds.BLOCK_SPRING_TRIGGER, SoundCategory.BLOCKS, 0.6f, 1f);
		}
	}

	@Override
	public void onLandedUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		entityIn.handleFallDamage(fallDistance, 0.0F);
	}
}