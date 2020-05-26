package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import hugman.mubble.objects.tile_entity.PresentTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
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
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context)
    {
		if(!state.get(EMPTY) && !state.get(OPEN))
		{
    		return FULL_SHAPE;
		}
		else
		{
			return EMPTY_SHAPE;
		}
    }
    
    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
    {
    	if(!worldIn.isClient)
    	{
    		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
    		if(tileEntity instanceof PresentTileEntity)
    		{
    			player.openContainer((PresentTileEntity) tileEntity);
    			player.incrementStat(Stats.OPEN_BARREL);
    		}
    	}
		return ActionResult.SUCCESS;
    }

	@Override
    public void onBlockRemoved(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
    	if(state.getBlock() != newState.getBlock())
    	{
    		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
    		if(tileEntity instanceof Inventory)
    		{
    			ItemScatterer.spawn(worldIn, pos, (Inventory) tileEntity);
    			worldIn.updateHorizontalAdjacent(pos, this);
    		}
    	}
    	super.onBlockRemoved(state, worldIn, pos, newState, isMoving);
    }
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if(tileEntity instanceof PresentTileEntity)
		{
			((PresentTileEntity) tileEntity).presentTick();
		}
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
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(stack.hasCustomName())
		{
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof PresentTileEntity)
			{
				((PresentTileEntity) tileEntity).setCustomName(stack.getName());
			}
		}
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calculateComparatorOutput(worldIn.getBlockEntity(pos));
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
    
    @Override
    public boolean hasBlockEntity()
    {
    	return true;
    }

	@Override
	public BlockEntity createBlockEntity(BlockView worldIn)
	{
		return new PresentTileEntity();
	}
}
