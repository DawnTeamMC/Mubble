package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

public class GrassBlock extends net.minecraft.block.GrassBlock
{
	/* Extension for missing features */
    public GrassBlock(Properties builder)
    {
        super(builder);
    }
    
    private static boolean func_196383_a(IWorldReader world, BlockPos pos)
    {
        BlockPos blockpos = pos.up();
        return world.getLight(blockpos) >= 4 || world.getBlockState(blockpos).getOpacity(world, blockpos) < world.getMaxLightLevel();
    }

    private static boolean func_196384_b(IWorldReader world, BlockPos pos)
    {
        BlockPos blockpos = pos.up();
        return world.getLight(blockpos) >= 4 && world.getBlockState(blockpos).getOpacity(world, blockpos) < world.getMaxLightLevel() && !world.getFluidState(blockpos).isTagged(FluidTags.WATER);
    }
    
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
    	Block dirt = Blocks.DIRT;
    	if(this == MubbleBlocks.GREEN_HILL_GRASS_BLOCK) dirt = MubbleBlocks.GREEN_HILL_DIRT;
        if (!world.isRemote)
        {
           if (!world.isAreaLoaded(pos, 3)) return;
           if (!func_196383_a(world, pos))
           {
              world.setBlockState(pos, dirt.getDefaultState());
           }
           else
           {
              if (world.getLight(pos.up()) >= 9)
              {
                 for(int i = 0; i < 4; ++i)
                 {
                    BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (!world.isBlockPresent(blockpos))
                    {
                       return;
                    }
                    if (world.getBlockState(blockpos).getBlock() == dirt && func_196384_b(world, blockpos))
                    {
                       world.setBlockState(blockpos, this.getDefaultState());
                    }
                 }
              }
           }
        }
    }
}
