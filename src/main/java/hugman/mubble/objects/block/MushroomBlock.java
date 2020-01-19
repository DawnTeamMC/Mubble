package hugman.mubble.objects.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class MushroomBlock extends net.minecraft.block.MushroomBlock
{
    public MushroomBlock(Block.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
    	return false;
    }
    
    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
    {
    	return;
    }
    
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
    {
    	return false;
    }
    
    @Override
    public boolean trySpawningBigMushroom(ServerWorld worldIn, BlockPos pos, BlockState state, Random rand)
    {
    	return false;
    }
}
