package hugman.mod.objects.block;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

public class BlockLeavesCarpet extends BlockBush
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	private final Block base;
	
    public BlockLeavesCarpet(Block base_block)
    {
        super(Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.1F).sound(SoundType.PLANT).doesNotBlockMovement());
        setRegistryName(base_block.getRegistryName() + "_carpet");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
        this.base = base_block;
    }
    
    public BlockLeavesCarpet(String name, Block base_block)
    {
        super(Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.1F).sound(SoundType.PLANT).doesNotBlockMovement());
        setRegistryName(Mubble.MOD_ID, name + "_leaves_carpet");
        MubbleBlocks.register(this, ItemGroup.DECORATIONS);
        this.base = base_block;
    }
    
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return SHAPE;
    }
    
    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return base.getOpacity(base.getDefaultState(), worldIn, pos);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
    	return base.getRenderLayer();
    }
    
    @Override
    public boolean isFullCube(IBlockState state) 
    {
    	return false;
    }
    
    @Override
    public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    
    @Override
    public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos)
    {
        EnumFacing enumfacing = EnumFacing.UP;
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        return iblockstate.isIn(BlockTags.LEAVES) || (iblockstate.getBlockFaceShape(worldIn, blockpos, enumfacing) == BlockFaceShape.SOLID && !isExceptBlockForAttachWithPiston(iblockstate.getBlock()));
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
    
    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
    {
    	base.getDrops(base.getDefaultState(), drops, world, pos, fortune);
    }
}
