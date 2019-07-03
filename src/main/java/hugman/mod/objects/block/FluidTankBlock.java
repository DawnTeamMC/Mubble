package hugman.mod.objects.block;

import hugman.mod.init.MubbleBlockStateProperties;
import hugman.mod.init.MubbleTags;
import hugman.mod.objects.block_state.properties.FluidLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class FluidTankBlock extends Block implements IWaterLoggable
{
	public static final BooleanProperty UP = SixWayBlock.UP;
	public static final BooleanProperty DOWN = SixWayBlock.DOWN;
	public static final BooleanProperty NORTH = SixWayBlock.NORTH;
	public static final BooleanProperty EAST = SixWayBlock.EAST;
	public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
	public static final BooleanProperty WEST = SixWayBlock.WEST;
	public static final EnumProperty<FluidLog> FLUIDLOG = MubbleBlockStateProperties.FLUIDLOG;
	private static final VoxelShape STONE0 = Block.makeCuboidShape(1.0D, 0.0D, 0.0D, 15.0D, 1.0D, 1.0D);
	private static final VoxelShape STONE1 = Block.makeCuboidShape(1.0D, 0.0D, 15.0D, 15.0D, 1.0D, 16.0D);
	private static final VoxelShape STONE2 = Block.makeCuboidShape(1.0D, 15.0D, 15.0D, 15.0D, 16.0D, 16.0D);
	private static final VoxelShape STONE3 = Block.makeCuboidShape(1.0D, 15.0D, 0.0D, 15.0D, 16.0D, 1.0D);
	private static final VoxelShape STONE4 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 16.0D);
	private static final VoxelShape STONE5 = Block.makeCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	private static final VoxelShape STONE6 = Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 1.0D, 16.0D, 16.0D);
	private static final VoxelShape STONE7 = Block.makeCuboidShape(15.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape STONE8 = Block.makeCuboidShape(0.0D, 1.0D, 0.0D, 1.0D, 15.0D, 1.0D);
	private static final VoxelShape STONE9 = Block.makeCuboidShape(15.0D, 1.0D, 0.0D, 16.0D, 15.0D, 1.0D);
	private static final VoxelShape STONE10 = Block.makeCuboidShape(15.0D, 1.0D, 15.0D, 16.0D, 15.0D, 16.0D);
	private static final VoxelShape STONE11 = Block.makeCuboidShape(0.0D, 1.0D, 15.0D, 1.0D, 15.0D, 16.0D);
	private static final VoxelShape STONE = VoxelShapes.or(STONE0, VoxelShapes.or(STONE1, VoxelShapes.or(STONE2, VoxelShapes.or(STONE3, VoxelShapes.or(STONE4, VoxelShapes.or(STONE5, VoxelShapes.or(STONE6, VoxelShapes.or(STONE7, VoxelShapes.or(STONE8, VoxelShapes.or(STONE9, VoxelShapes.or(STONE10, STONE11)))))))))));
	private static final VoxelShape GLASS_UP = Block.makeCuboidShape(1.0D, 15.75D, 1.0D, 15.0D, 16.0D, 15.0D);
	private static final VoxelShape GLASS_DOWN = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 0.25D, 15.0D);
	private static final VoxelShape GLASS_NORTH = Block.makeCuboidShape(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 0.25D);
	private static final VoxelShape GLASS_SOUTH = Block.makeCuboidShape(1.0D, 1.0D, 15.75D, 15.0D, 15.0D, 16.0D);
	private static final VoxelShape GLASS_EAST = Block.makeCuboidShape(15.75D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D);
	private static final VoxelShape GLASS_WEST = Block.makeCuboidShape(0.0D, 1.0D, 1.0D, 0.25D, 15.0D, 15.0D);
	
	
    public FluidTankBlock()
    {
        super(Properties.from(Blocks.STONE));
        this.setDefaultState(this.stateContainer.getBaseState().with(FLUIDLOG, FluidLog.EMPTY));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	VoxelShape shape = STONE;
    	if(state.get(UP)) shape = VoxelShapes.or(shape, GLASS_UP);
    	if(state.get(DOWN)) shape = VoxelShapes.or(shape, GLASS_DOWN);
    	if(state.get(NORTH)) shape = VoxelShapes.or(shape, GLASS_NORTH);
    	if(state.get(SOUTH)) shape = VoxelShapes.or(shape, GLASS_SOUTH);
    	if(state.get(EAST)) shape = VoxelShapes.or(shape, GLASS_EAST);
    	if(state.get(WEST)) shape = VoxelShapes.or(shape, GLASS_WEST);
		return shape;
	}
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder)
    {
    	builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, FLUIDLOG);
    }
    
    @Override
	public BlockRenderLayer getRenderLayer()
    {
    	return BlockRenderLayer.CUTOUT_MIPPED;
	}
    
    @Override
    public IFluidState getFluidState(BlockState state)
    {
    	if(state.get(FLUIDLOG) == FluidLog.WATER) return Fluids.WATER.getStillFluidState(false);
    	else if(state.get(FLUIDLOG) == FluidLog.LAVA) return Fluids.LAVA.getStillFluidState(false);
    	else return Fluids.EMPTY.getDefaultState();
	}
    
    @Override
    public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state)
    {
    	if(state.get(FLUIDLOG) == FluidLog.WATER)
    	{
    		worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.EMPTY), 3);
    		return Fluids.WATER;
    	}
    	else if(state.get(FLUIDLOG) == FluidLog.LAVA)
    	{
    		worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.EMPTY), 3);
    		return Fluids.LAVA;
    	}
    	else return Fluids.EMPTY;
    }

    @Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn)
	{
		if(state.get(FLUIDLOG) == FluidLog.EMPTY) return true;
		else return false;
	}

    @Override
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn)
	{
        if (state.get(FLUIDLOG) == FluidLog.EMPTY && (fluidStateIn.getFluid() == Fluids.WATER || fluidStateIn.getFluid() == Fluids.LAVA))
        {
        	if (!worldIn.isRemote())
        	{
        		if(fluidStateIn.getFluid() == Fluids.WATER) worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.WATER), 3);
        		else if(fluidStateIn.getFluid() == Fluids.LAVA) worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.LAVA), 3);
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
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        IFluidState fluidState = context.getWorld().getFluidState(context.getPos());
        BlockState blockState = this.getDefaultState();
    	if(fluidState == Fluids.WATER) return blockState.with(FLUIDLOG, FluidLog.WATER);
    	else if(fluidState == Fluids.LAVA) return blockState.with(FLUIDLOG, FluidLog.LAVA);
    	else return blockState.with(FLUIDLOG, FluidLog.EMPTY);
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
    	if(MubbleTags.Items.FLUID_BUCKETS.contains(player.getHeldItem(handIn).getItem()) && state.get(FLUIDLOG) == FluidLog.EMPTY) return false;
    	else if(player.getHeldItem(handIn).getItem() == Items.BUCKET && state.get(FLUIDLOG) != FluidLog.EMPTY) return false;
    	else
		{
	        float a = 0.01563f;
	        float b = 0.98437f;
	        double hitX = hit.getHitVec().getX();
	        double hitY = hit.getHitVec().getY();
	        double hitZ = hit.getHitVec().getZ();
			if(hitY > b) 
			{
				if(!worldIn.isRemote) permuteSide(state, worldIn, pos, UP);
				return true;
			}
			if(hitY < a) 
			{
				if(!worldIn.isRemote) permuteSide(state, worldIn, pos, DOWN);
				return true;
			}
			if(hitZ < a)
			{
				if(!worldIn.isRemote) permuteSide(state, worldIn, pos, NORTH);
				return true;
			}
			if(hitZ > b)
			{
				if(!worldIn.isRemote) permuteSide(state, worldIn, pos, SOUTH);
				return true;
			}
			if(hitX > b)
			{
				if(!worldIn.isRemote) permuteSide(state, worldIn, pos, EAST);
				return true;
			}
			if(hitX < a)
			{
				if(!worldIn.isRemote) permuteSide(state, worldIn, pos, WEST);
				return true;
			}
		}
		return false;
    }
    
    private void permuteSide(BlockState state, World worldIn, BlockPos pos, BooleanProperty property)
    {
        IFluidState fluidState = worldIn.getFluidState(pos);
		if(state.get(property)) worldIn.setBlockState(pos, state.with(property, false), 3);
		else worldIn.setBlockState(pos, state.with(property, true), 3);
		worldIn.getPendingFluidTicks().scheduleTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(worldIn));
		worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_GLASS_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}
