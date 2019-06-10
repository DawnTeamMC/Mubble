package hugman.mod.objects.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockMushroom extends net.minecraft.block.BlockMushroom
{
    public BlockMushroom(Block.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
    	return false;
    }
    
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
    	return;
    }
    
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
    	return false;
    }
    
    @Override
    public boolean generateBigMushroom(IWorld worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	return false;
    }
}
