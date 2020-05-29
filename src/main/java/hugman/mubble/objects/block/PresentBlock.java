package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import hugman.mubble.objects.block.block_entity.PresentBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class PresentBlock extends BlockWithEntity implements Waterloggable
{
	public static final BooleanProperty OPEN = Properties.OPEN;
	public static final BooleanProperty EMPTY = MubbleBlockStateProperties.EMPTY;

	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	protected static final VoxelShape EMPTY_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D);
	protected static final VoxelShape FULL_SHAPE = VoxelShapes.union(EMPTY_SHAPE, Block.createCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D));

	public PresentBlock(Settings builder)
	{
		super(builder);
		this.setDefaultState(this.stateManager.getDefaultState().with(OPEN, Boolean.valueOf(false)).with(EMPTY, Boolean.valueOf(true)).with(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		if (!state.get(EMPTY) && !state.get(OPEN))
		{
			return FULL_SHAPE;
		}
		else
		{
			return EMPTY_SHAPE;
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
	{
		if (world.isClient)
		{
			return ActionResult.SUCCESS;
		}
		if (!world.isClient)
		{
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PresentBlockEntity)
			{
				player.openHandledScreen((PresentBlockEntity) blockEntity);
				//TODO player.incrementStat(Stats.OPEN_BARREL);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify)
	{
		if (!state.isOf(newState.getBlock()))
		{
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory)
			{
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, notify);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
		if (blockEntity instanceof PresentBlockEntity)
		{
			((PresentBlockEntity) blockEntity).tick();
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView worldIn)
	{
		return new PresentBlockEntity();
	}

	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if (stack.hasCustomName())
		{
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);
			if (blockEntity instanceof PresentBlockEntity)
			{
				((PresentBlockEntity) blockEntity).setCustomName(stack.getName());
			}
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos)
	{
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(context.getWorld().getFluidState(context.getBlockPos()).getFluid() == Fluids.WATER));
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder)
	{
		builder.add(OPEN, EMPTY, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state)
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
	}
}
