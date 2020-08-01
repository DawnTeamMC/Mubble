package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleBlockPack;
import com.hugman.mubble.object.block.block_state_property.FluidLog;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class FluidTankBlock extends Block implements FluidDrainable, FluidFillable {
	public static final BooleanProperty UP = Properties.UP;
	public static final BooleanProperty DOWN = Properties.DOWN;
	public static final BooleanProperty NORTH = Properties.NORTH;
	public static final BooleanProperty EAST = Properties.EAST;
	public static final BooleanProperty SOUTH = Properties.SOUTH;
	public static final BooleanProperty WEST = Properties.WEST;
	public static final EnumProperty<FluidLog> FLUIDLOG = MubbleBlockPack.Properties.FLUIDLOG;

	private static final VoxelShape GLASS_UP = Block.createCuboidShape(1.0D, 15.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	private static final VoxelShape GLASS_DOWN = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);
	private static final VoxelShape GLASS_NORTH = Block.createCuboidShape(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 1.0D);
	private static final VoxelShape GLASS_SOUTH = Block.createCuboidShape(1.0D, 1.0D, 15.0D, 15.0D, 15.0D, 16.0D);
	private static final VoxelShape GLASS_EAST = Block.createCuboidShape(15.0D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D);
	private static final VoxelShape GLASS_WEST = Block.createCuboidShape(0.0D, 1.0D, 1.0D, 1.0D, 15.0D, 15.0D);
	private static final VoxelShape FULL_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.createCuboidShape(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D), BooleanBiFunction.ONLY_FIRST);


	public FluidTankBlock(Settings builder) {
		super(builder);
		this.setDefaultState(this.stateManager.getDefaultState().with(FLUIDLOG, FluidLog.EMPTY));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
		return FULL_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
		VoxelShape shape = FULL_SHAPE;
		if(!state.get(UP)) {
			shape = VoxelShapes.combineAndSimplify(shape, GLASS_UP, BooleanBiFunction.ONLY_FIRST);
		}
		if(!state.get(DOWN)) {
			shape = VoxelShapes.combineAndSimplify(shape, GLASS_DOWN, BooleanBiFunction.ONLY_FIRST);
		}
		if(!state.get(NORTH)) {
			shape = VoxelShapes.combineAndSimplify(shape, GLASS_NORTH, BooleanBiFunction.ONLY_FIRST);
		}
		if(!state.get(SOUTH)) {
			shape = VoxelShapes.combineAndSimplify(shape, GLASS_SOUTH, BooleanBiFunction.ONLY_FIRST);
		}
		if(!state.get(EAST)) {
			shape = VoxelShapes.combineAndSimplify(shape, GLASS_EAST, BooleanBiFunction.ONLY_FIRST);
		}
		if(!state.get(WEST)) {
			shape = VoxelShapes.combineAndSimplify(shape, GLASS_WEST, BooleanBiFunction.ONLY_FIRST);
		}
		return shape;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, FLUIDLOG);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		if(state.get(FLUIDLOG) == FluidLog.WATER) {
			return Fluids.WATER.getStill(false);
		}
		else if(state.get(FLUIDLOG) == FluidLog.LAVA) {
			return Fluids.LAVA.getStill(false);
		}
		else {
			return Fluids.EMPTY.getDefaultState();
		}
	}

	@Override
	public Fluid tryDrainFluid(WorldAccess worldIn, BlockPos pos, BlockState state) {
		if(state.get(FLUIDLOG) == FluidLog.WATER) {
			worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.EMPTY), 3);
			return Fluids.WATER;
		}
		else if(state.get(FLUIDLOG) == FluidLog.LAVA) {
			worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.EMPTY), 3);
			return Fluids.LAVA;
		}
		else {
			return Fluids.EMPTY;
		}
	}

	@Override
	public boolean canFillWithFluid(BlockView worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return state.get(FLUIDLOG) == FluidLog.EMPTY;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		Fluid fluid = fluidStateIn.getFluid();
		if(state.get(FLUIDLOG) == FluidLog.EMPTY && (fluid == Fluids.WATER || fluid == Fluids.LAVA)) {
			if(!worldIn.isClient()) {
				if(fluid == Fluids.WATER) {
					worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.WATER), 3);
				}
				else if(fluid == Fluids.LAVA) {
					worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.LAVA), 3);
				}
				worldIn.getFluidTickScheduler().schedule(pos, fluid, fluid.getTickRate(worldIn));
			}
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Fluid fluid = context.getWorld().getFluidState(context.getBlockPos()).getFluid();
		BlockState blockState = this.getDefaultState();
		if(fluid == Fluids.WATER) {
			return blockState.with(FLUIDLOG, FluidLog.WATER);
		}
		else if(fluid == Fluids.LAVA) {
			return blockState.with(FLUIDLOG, FluidLog.LAVA);
		}
		else {
			return blockState.with(FLUIDLOG, FluidLog.EMPTY);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit) {
		ItemStack itemStackIn = player.getStackInHand(handIn);
		Item itemIn = itemStackIn.getItem();
		if(itemIn instanceof BucketItem) {
			BucketItem bucket = (BucketItem) itemIn;
			if(bucket == Fluids.EMPTY.getBucketItem() && state.get(FLUIDLOG) != FluidLog.EMPTY) {
				return ActionResult.PASS;
			}
			else if(bucket != Fluids.EMPTY.getBucketItem() && state.get(FLUIDLOG) == FluidLog.EMPTY) {
				return ActionResult.PASS;
			}
		}
		if(itemIn instanceof BlockItem) {
			BlockItem blockItem = (BlockItem) itemIn;
			if(blockItem.getBlock() instanceof FluidTankBlock) {
				return ActionResult.PASS;
			}
		}
		else {
			float a = 0.0626f;
			float b = 0.9374f;
			double hitX = hit.getPos().getX() - (double) pos.getX();
			double hitY = hit.getPos().getY() - (double) pos.getY();
			double hitZ = hit.getPos().getZ() - (double) pos.getZ();
			if(hitY > b) {
				if(!worldIn.isClient) {
					permuteSide(state, worldIn, pos, UP);
				}
				return ActionResult.SUCCESS;
			}
			if(hitY < a) {
				if(!worldIn.isClient) {
					permuteSide(state, worldIn, pos, DOWN);
				}
				return ActionResult.SUCCESS;
			}
			if(hitZ < a) {
				if(!worldIn.isClient) {
					permuteSide(state, worldIn, pos, NORTH);
				}
				return ActionResult.SUCCESS;
			}
			if(hitZ > b) {
				if(!worldIn.isClient) {
					permuteSide(state, worldIn, pos, SOUTH);
				}
				return ActionResult.SUCCESS;
			}
			if(hitX > b) {
				if(!worldIn.isClient) {
					permuteSide(state, worldIn, pos, EAST);
				}
				return ActionResult.SUCCESS;
			}
			if(hitX < a) {
				if(!worldIn.isClient) {
					permuteSide(state, worldIn, pos, WEST);
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	private void permuteSide(BlockState state, World worldIn, BlockPos pos, BooleanProperty property) {
		FluidState fluidState = worldIn.getFluidState(pos);
		if(state.get(property)) {
			worldIn.setBlockState(pos, state.with(property, false), 3);
		}
		else {
			worldIn.setBlockState(pos, state.with(property, true), 3);
		}
		worldIn.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(worldIn));
		worldIn.playSound(null, pos, SoundEvents.BLOCK_GLASS_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}
