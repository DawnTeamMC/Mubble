package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockCloud extends Block
{
    public BlockCloud(String color)
    {
        super(Properties.create(Material.GLASS).sound(SoundType.CLOTH).hardnessAndResistance(0f));
        setRegistryName(Reference.MOD_ID, color + "_cloud_block");
        MubbleBlocks.register(this);
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
    	return false;
    }
    
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return VoxelShapes.empty();
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
    	return BlockFaceShape.UNDEFINED;
	}
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
    	return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return 0;
    }
    
    @Override
    public int quantityDropped(IBlockState state, Random random)
    {
    	return 0;
    }
}
