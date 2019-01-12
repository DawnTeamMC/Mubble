package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.init.BlockInit;
import hugman.mod.init.CreativeTabInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpring extends BlockBase implements IHasModel
{
	private static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5625D, 0.9375D);
	private static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 0.9375D, 1.0D, 0.9375D);
	private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0625D, 0.0625D, 0.4375D, 0.9375D, 0.9375D, 1.0D);
	private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0625D, 0.0625D, 0.0D, 0.9375D, 0.9375D, 0.5625D);
	private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.4375D, 0.0625D, 0.0625D, 1.0D, 0.9375D, 0.9375D);
	private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0625D, 0.0625D, 0.5625D, 0.9375D, 0.9375D);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    
	/**
	 * Static class - can only be initialized once.
	 */
	public BlockSpring()
	{
		super("spring", CreativeTabInit.SONIC, Material.IRON, 4f, 20f, SoundType.METAL);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        state = this.getActualState(state, source, pos);
        if(state.getValue(FACING) == EnumFacing.UP) return UP_AABB;
        else if(state.getValue(FACING) == EnumFacing.DOWN) return DOWN_AABB;
        else if(state.getValue(FACING) == EnumFacing.NORTH) return NORTH_AABB;
        else if(state.getValue(FACING) == EnumFacing.SOUTH) return SOUTH_AABB;
        else if(state.getValue(FACING) == EnumFacing.WEST) return WEST_AABB;
        else if(state.getValue(FACING) == EnumFacing.EAST) return EAST_AABB;
        else return UP_AABB;
    }
	
	@Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, facing);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
        return i;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7));
    }
	
	@Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
	
	@Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }
}
