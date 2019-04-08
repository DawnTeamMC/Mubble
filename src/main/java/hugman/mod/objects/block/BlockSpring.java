package hugman.mod.objects.block;

import hugman.mod.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

public class BlockSpring extends BlockDirectional implements IBucketPickupHandler, ILiquidContainer
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
	
    public BlockSpring()
    {
        super("spring", Properties.create(Material.IRON).hardnessAndResistance(4f), ItemGroup.TRANSPORTATION);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.UP).with(WATERLOGGED, Boolean.valueOf(false)));
    }
    
    @Override
    public EnumPushReaction getPushReaction(IBlockState state)
    {
    	return EnumPushReaction.DESTROY;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, IBlockState> builder)
    {
    	builder.add(WATERLOGGED);
    	super.fillStateContainer(builder);
    }
    
    @Override
    public boolean propagatesSkylightDown(IBlockState state, IBlockReader reader, BlockPos pos)
    {
    	return true;
    }
    
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
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
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
    	return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
    	return false;
    }
    
    @Override
    public Fluid pickupFluid(IWorld worldIn, BlockPos pos, IBlockState state)
    {
    	if (state.get(WATERLOGGED))
    	{
    		worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(false)), 3);
    		return Fluids.WATER;
    	}
    	else return Fluids.EMPTY;
    }
    
	@Override
    public IFluidState getFluidState(IBlockState state)
    {
    	return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
    }
    
    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, IBlockState state, Fluid fluidIn)
    {
    	return !state.get(WATERLOGGED) && fluidIn == Fluids.WATER;
    }
    
    @Override
    public boolean receiveFluid(IWorld worldIn, BlockPos pos, IBlockState state, IFluidState fluidStateIn)
    {
    	if (!state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER)
    	{
    		if (!worldIn.isRemote())
    		{
    			worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)), 3);
    			worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
    		}
    		return true;
    	}
    	else return false;
    }
    
    @Override
    public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos)
    {
        EnumFacing enumfacing = state.get(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(worldIn, blockpos, enumfacing) == BlockFaceShape.SOLID && !isExceptBlockForAttachWithPiston(iblockstate.getBlock());
    }
    
    @Override
    public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return facing.getOpposite() == stateIn.get(FACING) && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
    }
    
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context)
    {
        IFluidState fluidState = context.getWorld().getFluidState(context.getPos());
        IBlockState blockState = this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(fluidState.getFluid() == Fluids.WATER));
        return blockState.with(FACING, context.getFace());
    }
    
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	switch (state.get(FACING))
    	{
		case UP:
			entityIn.motionY = 1.5D;
			break;
		case DOWN:
			entityIn.motionY = -1.5D;
			break;
		case NORTH:
			entityIn.motionZ = -1.5D;
			entityIn.motionY = 0.2D;
			break;
		case SOUTH:
			entityIn.motionZ = 1.5D;
			entityIn.motionY = 0.2D;
			break;
		case EAST:
			entityIn.motionX = 1.5D;
			entityIn.motionY = 0.2D;
			break;
		case WEST:
			entityIn.motionX = -1.5D;
			entityIn.motionY = 0.2D;
			break;
		default:
			break;
		}
    	entityIn.fallDistance = 0f;
    	worldIn.playSound((EntityPlayer)null, pos, MubbleSounds.BLOCK_SPRING_TRIGGER, SoundCategory.BLOCKS, 1f, 1f);
    }
    
    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
    	entityIn.fall(fallDistance, 0.0F);
    }
}