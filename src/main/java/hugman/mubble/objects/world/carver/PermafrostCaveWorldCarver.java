package hugman.mubble.objects.world.carver;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.NetherCaveCarver;

public class PermafrostCaveWorldCarver extends NetherCaveCarver
{
    public PermafrostCaveWorldCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> p_i49927_1_)
    {
        super(p_i49927_1_);
        this.alwaysCarvableBlocks = ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.GRASS_BLOCK, MubbleBlocks.PERMAROCK);
        this.carvableFluids = ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
    }
    
    @Override
    protected boolean carveAtPoint(Chunk chunkIn, Function<BlockPos, Biome> function, BitSet carvingMask, Random rand, BlockPos.Mutable mutable_1, BlockPos.Mutable mutable_2, BlockPos.Mutable mutable_3, int mainChunkX, int mainChunkZ, int i, int j, int k, int l, int m, int n, AtomicBoolean atomicBoolean)
    {
    	int o = l | n << 4 | m << 8;
    	if (carvingMask.get(o))
    	{
    		return false;
    	}
    	else
    	{
    		carvingMask.set(o);
    		mutable_1.set(j, m, k);
    		if (this.canAlwaysCarveBlock(chunkIn.getBlockState(mutable_1)))
    		{
    			BlockState blockstate;
    			if (m <= 31)
    			{
    				blockstate = WATER.getBlockState();
    			}
    			else
    			{
    				blockstate = CAVE_AIR;
    			}
             chunkIn.setBlockState(mutable_1, blockstate, false);
             return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    }
}