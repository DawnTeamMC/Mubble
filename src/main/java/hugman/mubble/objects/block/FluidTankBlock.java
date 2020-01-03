package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleBlockStateProperties;
import hugman.mubble.objects.block.block_state_property.FluidLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class FluidTankBlock extends Block implements IBucketPickupHandler, ILiquidContainer
{
	public static final BooleanProperty UP = SixWayBlock.UP;
	public static final BooleanProperty DOWN = SixWayBlock.DOWN;
	public static final BooleanProperty NORTH = SixWayBlock.NORTH;
	public static final BooleanProperty EAST = SixWayBlock.EAST;
	public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
	public static final BooleanProperty WEST = SixWayBlock.WEST;
	public static final EnumProperty<FluidLog> FLUIDLOG = MubbleBlockStateProperties.FLUIDLOG;

	private static final VoxelShape GLASS_UP = Block.makeCuboidShape(1.0D, 15.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	private static final VoxelShape GLASS_DOWN = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);
	private static final VoxelShape GLASS_NORTH = Block.makeCuboidShape(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 1.0D);
	private static final VoxelShape GLASS_SOUTH = Block.makeCuboidShape(1.0D, 1.0D, 15.0D, 15.0D, 15.0D, 16.0D);
	private static final VoxelShape GLASS_EAST = Block.makeCuboidShape(15.0D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D);
	private static final VoxelShape GLASS_WEST = Block.makeCuboidShape(0.0D, 1.0D, 1.0D, 1.0D, 15.0D, 15.0D);
	private static final VoxelShape FULL_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.makeCuboidShape(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D), IBooleanFunction.ONLY_FIRST);
	
	
    public FluidTankBlock(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FLUIDLOG, FluidLog.EMPTY));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	return FULL_SHAPE;
	}
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	VoxelShape shape = FULL_SHAPE;
    	if(!state.get(UP)) shape = VoxelShapes.combineAndSimplify(shape, GLASS_UP, IBooleanFunction.ONLY_FIRST);
    	if(!state.get(DOWN)) shape = VoxelShapes.combineAndSimplify(shape, GLASS_DOWN, IBooleanFunction.ONLY_FIRST);
    	if(!state.get(NORTH)) shape = VoxelShapes.combineAndSimplify(shape, GLASS_NORTH, IBooleanFunction.ONLY_FIRST);
    	if(!state.get(SOUTH)) shape = VoxelShapes.combineAndSimplify(shape, GLASS_SOUTH, IBooleanFunction.ONLY_FIRST);
    	if(!state.get(EAST)) shape = VoxelShapes.combineAndSimplify(shape, GLASS_EAST, IBooleanFunction.ONLY_FIRST);
    	if(!state.get(WEST)) shape = VoxelShapes.combineAndSimplify(shape, GLASS_WEST, IBooleanFunction.ONLY_FIRST);
		return shape;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder)
    {
    	builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, FLUIDLOG);
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
    	Fluid fluid = fluidStateIn.getFluid();
        if (state.get(FLUIDLOG) == FluidLog.EMPTY && (fluid == Fluids.WATER || fluid == Fluids.LAVA))
        {
        	if (!worldIn.isRemote())
        	{
        		if(fluid == Fluids.WATER) worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.WATER), 3);
        		else if(fluid == Fluids.LAVA) worldIn.setBlockState(pos, state.with(FLUIDLOG, FluidLog.LAVA), 3);
        		worldIn.getPendingFluidTicks().scheduleTick(pos, fluid, fluid.getTickRate(worldIn));
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
        Fluid fluid = context.getWorld().getFluidState(context.getPos()).getFluid();
        BlockState blockState = this.getDefaultState();
    	if(fluid == Fluids.WATER) return blockState.with(FLUIDLOG, FluidLog.WATER);
    	else if(fluid == Fluids.LAVA) return blockState.with(FLUIDLOG, FluidLog.LAVA);
    	else return blockState.with(FLUIDLOG, FluidLog.EMPTY);
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
    	ItemStack itemStackIn = player.getHeldItem(handIn);
    	Item itemIn = itemStackIn.getItem();
    	if(itemIn instanceof BucketItem)
    	{
    		BucketItem bucket = (BucketItem)itemIn;
    		if(bucket.getFluid() == Fluids.EMPTY && state.get(FLUIDLOG) != FluidLog.EMPTY)
    		{
    			return false;
    		}
    		else if(bucket.getFluid() != Fluids.EMPTY && state.get(FLUIDLOG) == FluidLog.EMPTY)
    		{
    			return false;
    		}
    	}
    	if(itemIn instanceof BlockItem)
    	{
    		BlockItem blockItem = (BlockItem)itemIn;
    		if(blockItem.getBlock() instanceof FluidTankBlock)
    		{
    			return false;
    		}
    	}
    	else
		{
	        float a = 0.0626f;
	        float b = 0.9374f;
	        double hitX = hit.getHitVec().getX() - (double)pos.getX();
	        double hitY = hit.getHitVec().getY() - (double)pos.getY();
	        double hitZ = hit.getHitVec().getZ() - (double)pos.getZ();
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
