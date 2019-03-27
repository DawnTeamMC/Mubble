package hugman.mod.objects.block;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlockStateProperties;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.state.properties.SlabVerticalType;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockSlabVertical extends Block implements IBucketPickupHandler, ILiquidContainer
{
	public static final EnumProperty<SlabVerticalType> TYPE = MubbleBlockStateProperties.VERTICAL_SLAB_TYPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape NORTH_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape EAST_SHAPE = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape WEST_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
	
    public BlockSlabVertical(String name, Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(Mubble.MOD_ID, name + "_vertical_slab");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
    
    public BlockSlabVertical(Block base_block)
    {
        super(Properties.from(base_block));
        setRegistryName(base_block.getRegistryName() + "_vertical_slab");
        MubbleBlocks.register(this, ItemGroup.BUILDING_BLOCKS);
    }
    
    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return worldIn.getMaxLightLevel();
	}

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
       builder.add(TYPE, WATERLOGGED);
	}

    @Override
	protected boolean canSilkHarvest()
	{
		return false;
	}

	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		SlabVerticalType slabtype = state.get(TYPE);
		switch(slabtype)
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
	public boolean isTopSolid(IBlockState state)
	{
		return state.get(TYPE) == SlabVerticalType.DOUBLE;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		SlabVerticalType slabtype = state.get(TYPE);
		if (slabtype == SlabVerticalType.DOUBLE)
		{
           return BlockFaceShape.SOLID;
        }
		else if (face == EnumFacing.NORTH && slabtype == SlabVerticalType.NORTH)
        {
           return BlockFaceShape.SOLID;
        }
		else if (face == EnumFacing.SOUTH && slabtype == SlabVerticalType.SOUTH)
        {
           return BlockFaceShape.SOLID;
        }
		else if (face == EnumFacing.EAST && slabtype == SlabVerticalType.EAST)
        {
           return BlockFaceShape.SOLID;
        }
		else
        {
           return face == EnumFacing.WEST && slabtype == SlabVerticalType.WEST ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
	}

	@Nullable
	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		IBlockState iblockstate = context.getWorld().getBlockState(context.getPos());
        if (iblockstate.getBlock() == this)
        {
        	return iblockstate.with(TYPE, SlabVerticalType.DOUBLE).with(WATERLOGGED, Boolean.valueOf(false));
        }
        else
        {
        	IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        	IBlockState iblockstate1 = this.getDefaultState().with(TYPE, SlabVerticalType.NORTH).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
        	EnumFacing facing = context.getPlacementHorizontalFacing();
        	EnumFacing face_hit = context.getFace();
        	if(facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH)
        	{
        		if(face_hit == EnumFacing.SOUTH) return iblockstate1.with(TYPE, SlabVerticalType.NORTH);
        		else if(face_hit == EnumFacing.NORTH) return iblockstate1.with(TYPE, SlabVerticalType.SOUTH);
        		else if((double)context.getHitZ() > 0.5D) return iblockstate1.with(TYPE, SlabVerticalType.SOUTH);
        		else return iblockstate1.with(TYPE, SlabVerticalType.NORTH);
        	}
        	else if (facing == EnumFacing.EAST || facing == EnumFacing.WEST)
        	{
        		if(face_hit == EnumFacing.WEST) return iblockstate1.with(TYPE, SlabVerticalType.EAST);
        		else if(face_hit == EnumFacing.EAST) return iblockstate1.with(TYPE, SlabVerticalType.WEST);
        		else if((double)context.getHitX() > 0.5D) return iblockstate1.with(TYPE, SlabVerticalType.EAST);
        		else return iblockstate1.with(TYPE, SlabVerticalType.WEST);
        	}
        	else return iblockstate1;
        }
    }
	
	@Override
    public int quantityDropped(IBlockState state, Random random)
    {
        return state.get(TYPE) == SlabVerticalType.DOUBLE ? 2 : 1;
    }
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return state.get(TYPE) == SlabVerticalType.DOUBLE;
    }

	@Override
    public boolean isReplaceable(IBlockState state, BlockItemUseContext context)
	{
        ItemStack itemstack = context.getItem();
        SlabVerticalType slabtype = state.get(TYPE);
        if (slabtype != SlabVerticalType.DOUBLE && itemstack.getItem() == this.asItem())
        {
           if (context.replacingClickedOnBlock())
           {
              boolean flag1 = (double)context.getHitZ() > 0.5D;
              boolean flag2 = (double)context.getHitX() > 0.5D;
              EnumFacing enumfacing = context.getFace();
              if (slabtype == SlabVerticalType.NORTH)
              {
                 return enumfacing == EnumFacing.SOUTH || flag1 && enumfacing.getAxis().isHorizontal();
              }
              if (slabtype == SlabVerticalType.SOUTH)
              {
                 return enumfacing == EnumFacing.NORTH || !flag1 && enumfacing.getAxis().isHorizontal();
              }
              if (slabtype == SlabVerticalType.EAST)
              {
                 return enumfacing == EnumFacing.WEST || !flag2 && enumfacing.getAxis().isHorizontal();
              }
              else
              {
                 return enumfacing == EnumFacing.EAST || flag2 && enumfacing.getAxis().isHorizontal();
              }
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
	public Fluid pickupFluid(IWorld worldIn, BlockPos pos, IBlockState state)
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

	@SuppressWarnings("deprecation")
	@Override
	public IFluidState getFluidState(IBlockState state)
	{
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, IBlockState state, Fluid fluidIn)
	{
        return state.get(TYPE) != SlabVerticalType.DOUBLE && !state.get(WATERLOGGED) && fluidIn == Fluids.WATER;
	}

	@Override
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, IBlockState state, IFluidState fluidStateIn)
	{
        if (state.get(TYPE) != SlabVerticalType.DOUBLE && !state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER)
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
	
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
        if (stateIn.get(WATERLOGGED))
        {
           worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean allowsMovement(IBlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
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
