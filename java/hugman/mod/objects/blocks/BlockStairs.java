package hugman.mod.objects.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends BlockBase implements IHasModel
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<BlockStairs.EnumHalf> HALF = PropertyEnum.<BlockStairs.EnumHalf>create("half", BlockStairs.EnumHalf.class);
    public static final PropertyEnum<BlockStairs.EnumShape> SHAPE = PropertyEnum.<BlockStairs.EnumShape>create("shape", BlockStairs.EnumShape.class);
    protected static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_WEST = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_EAST = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_NORTH = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_TOP_NW = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_TOP_NE = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_TOP_SW = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_TOP_SE = new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_EAST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_QTR_BOT_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_BOT_NW = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_BOT_NE = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_BOT_SW = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_BOT_SE = new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockStairs(String name, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(name + "_stairs", material, hardness, resistance / 1.5F, sound, light);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT));
        this.setLightOpacity(255);
	}
	
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockStairs(String name, Material material, float hardness, float resistance, SoundType sound)
	{
		super(name + "_stairs", material, hardness, resistance / 1.5F, sound);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT));
        this.setLightOpacity(255);
	}
	
	@Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        if (!isActualState)
        {
            state = this.getActualState(state, worldIn, pos);
        }

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }
    
    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState bstate)
    {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        boolean flag = bstate.getValue(HALF) == BlockStairs.EnumHalf.TOP;
        list.add(flag ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
        BlockStairs.EnumShape blockstairs$enumshape = (BlockStairs.EnumShape)bstate.getValue(SHAPE);

        if (blockstairs$enumshape == BlockStairs.EnumShape.STRAIGHT || blockstairs$enumshape == BlockStairs.EnumShape.INNER_LEFT || blockstairs$enumshape == BlockStairs.EnumShape.INNER_RIGHT)
        {
            list.add(getCollQuarterBlock(bstate));
        }

        if (blockstairs$enumshape != BlockStairs.EnumShape.STRAIGHT)
        {
            list.add(getCollEighthBlock(bstate));
        }

        return list;
    }
    
    private static AxisAlignedBB getCollQuarterBlock(IBlockState bstate)
    {
        boolean flag = bstate.getValue(HALF) == BlockStairs.EnumHalf.TOP;

        switch ((EnumFacing)bstate.getValue(FACING))
        {
            case NORTH:
            default:
                return flag ? AABB_QTR_BOT_NORTH : AABB_QTR_TOP_NORTH;
            case SOUTH:
                return flag ? AABB_QTR_BOT_SOUTH : AABB_QTR_TOP_SOUTH;
            case WEST:
                return flag ? AABB_QTR_BOT_WEST : AABB_QTR_TOP_WEST;
            case EAST:
                return flag ? AABB_QTR_BOT_EAST : AABB_QTR_TOP_EAST;
        }
    }
    
    private static AxisAlignedBB getCollEighthBlock(IBlockState bstate)
    {
        EnumFacing enumfacing = (EnumFacing)bstate.getValue(FACING);
        EnumFacing enumfacing1;

        switch ((BlockStairs.EnumShape)bstate.getValue(SHAPE))
        {
            case OUTER_LEFT:
            default:
                enumfacing1 = enumfacing;
                break;
            case OUTER_RIGHT:
                enumfacing1 = enumfacing.rotateY();
                break;
            case INNER_RIGHT:
                enumfacing1 = enumfacing.getOpposite();
                break;
            case INNER_LEFT:
                enumfacing1 = enumfacing.rotateYCCW();
        }

        boolean flag = bstate.getValue(HALF) == BlockStairs.EnumHalf.TOP;

        switch (enumfacing1)
        {
            case NORTH:
            default:
                return flag ? AABB_OCT_BOT_NW : AABB_OCT_TOP_NW;
            case SOUTH:
                return flag ? AABB_OCT_BOT_SE : AABB_OCT_TOP_SE;
            case WEST:
                return flag ? AABB_OCT_BOT_SW : AABB_OCT_TOP_SW;
            case EAST:
                return flag ? AABB_OCT_BOT_NE : AABB_OCT_TOP_NE;
        }
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        state = this.getActualState(state, worldIn, pos);

        if (face.getAxis() == EnumFacing.Axis.Y)
        {
            return face == EnumFacing.UP == (state.getValue(HALF) == BlockStairs.EnumHalf.TOP) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
        else
        {
            BlockStairs.EnumShape blockstairs$enumshape = (BlockStairs.EnumShape)state.getValue(SHAPE);

            if (blockstairs$enumshape != BlockStairs.EnumShape.OUTER_LEFT && blockstairs$enumshape != BlockStairs.EnumShape.OUTER_RIGHT)
            {
                EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

                switch (blockstairs$enumshape)
                {
                    case INNER_RIGHT:
                        return enumfacing != face && enumfacing != face.rotateYCCW() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
                    case INNER_LEFT:
                        return enumfacing != face && enumfacing != face.rotateY() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
                    case STRAIGHT:
                        return enumfacing == face ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
                    default:
                        return BlockFaceShape.UNDEFINED;
                }
            }
            else
            {
                return BlockFaceShape.UNDEFINED;
            }
        }
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return state.getValue(HALF) == BlockStairs.EnumHalf.TOP;
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing()).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate.withProperty(HALF, BlockStairs.EnumHalf.BOTTOM) : iblockstate.withProperty(HALF, BlockStairs.EnumHalf.TOP);
    }
    
    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
    {
        List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(this.getActualState(blockState, worldIn, pos)))
        {
            list.add(this.rayTrace(pos, start, end, axisalignedbb));
        }

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for (RayTraceResult raytraceresult : list)
        {
            if (raytraceresult != null)
            {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1)
                {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(HALF, (meta & 4) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.byIndex(5 - (meta & 3)));
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        if (state.getValue(HALF) == BlockStairs.EnumHalf.TOP)
        {
            i |= 4;
        }

        i = i | 5 - ((EnumFacing)state.getValue(FACING)).getIndex();
        return i;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(SHAPE, getStairsShape(state, worldIn, pos));
    }
    
    private static BlockStairs.EnumShape getStairsShape(IBlockState state, IBlockAccess block, BlockPos pos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        IBlockState iblockstate = block.getBlockState(pos.offset(enumfacing));

        if (isBlockStairs(iblockstate) && state.getValue(HALF) == iblockstate.getValue(HALF))
        {
            EnumFacing enumfacing1 = (EnumFacing)iblockstate.getValue(FACING);

            if (enumfacing1.getAxis() != ((EnumFacing)state.getValue(FACING)).getAxis() && isDifferentStairs(state, block, pos, enumfacing1.getOpposite()))
            {
                if (enumfacing1 == enumfacing.rotateYCCW())
                {
                    return BlockStairs.EnumShape.OUTER_LEFT;
                }

                return BlockStairs.EnumShape.OUTER_RIGHT;
            }
        }

        IBlockState iblockstate1 = block.getBlockState(pos.offset(enumfacing.getOpposite()));

        if (isBlockStairs(iblockstate1) && state.getValue(HALF) == iblockstate1.getValue(HALF))
        {
            EnumFacing enumfacing2 = (EnumFacing)iblockstate1.getValue(FACING);

            if (enumfacing2.getAxis() != ((EnumFacing)state.getValue(FACING)).getAxis() && isDifferentStairs(state, block, pos, enumfacing2))
            {
                if (enumfacing2 == enumfacing.rotateYCCW())
                {
                    return BlockStairs.EnumShape.INNER_LEFT;
                }

                return BlockStairs.EnumShape.INNER_RIGHT;
            }
        }

        return BlockStairs.EnumShape.STRAIGHT;
    }
    
    private static boolean isDifferentStairs(IBlockState state, IBlockAccess block, BlockPos pos, EnumFacing face)
    {
        IBlockState iblockstate = block.getBlockState(pos.offset(face));
        return !isBlockStairs(iblockstate) || iblockstate.getValue(FACING) != state.getValue(FACING) || iblockstate.getValue(HALF) != state.getValue(HALF);
    }

    public static boolean isBlockStairs(IBlockState state)
    {
        return state.getBlock() instanceof BlockStairs;
    }
    
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
    
    @Override
    @SuppressWarnings("incomplete-switch")
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        BlockStairs.EnumShape blockstairs$enumshape = (BlockStairs.EnumShape)state.getValue(SHAPE);

        switch (mirrorIn)
        {
            case LEFT_RIGHT:

                if (enumfacing.getAxis() == EnumFacing.Axis.Z)
                {
                    switch (blockstairs$enumshape)
                    {
                        case OUTER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
                        case INNER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                        case INNER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                        default:
                            return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }

                break;
            case FRONT_BACK:

                if (enumfacing.getAxis() == EnumFacing.Axis.X)
                {
                    switch (blockstairs$enumshape)
                    {
                        case OUTER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
                        case INNER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                        case INNER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                        case STRAIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }
        }

        return super.withMirror(state, mirrorIn);
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, HALF, SHAPE});
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        if (net.minecraftforge.common.ForgeModContainer.disableStairSlabCulling)
            return super.doesSideBlockRendering(state, world, pos, face);

        if ( state.isOpaqueCube() )
            return true;

        state = this.getActualState(state, world, pos);

        EnumHalf half = state.getValue(HALF);
        EnumFacing side = state.getValue(FACING);
        EnumShape shape = state.getValue(SHAPE);
        if (face == EnumFacing.UP) return half == EnumHalf.TOP;
        if (face == EnumFacing.DOWN) return half == EnumHalf.BOTTOM;
        if (shape == EnumShape.OUTER_LEFT || shape == EnumShape.OUTER_RIGHT) return false;
        if (face == side) return true;
        if (shape == EnumShape.INNER_LEFT && face.rotateY() == side) return true;
        if (shape == EnumShape.INNER_RIGHT && face.rotateYCCW() == side) return true;
        return false;
    }
    
    public static enum EnumHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        private EnumHalf(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
    
    public static enum EnumShape implements IStringSerializable
    {
        STRAIGHT("straight"),
        INNER_LEFT("inner_left"),
        INNER_RIGHT("inner_right"),
        OUTER_LEFT("outer_left"),
        OUTER_RIGHT("outer_right");

        private final String name;

        private EnumShape(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}