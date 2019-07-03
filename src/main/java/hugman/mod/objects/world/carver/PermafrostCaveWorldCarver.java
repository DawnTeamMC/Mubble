package hugman.mod.objects.world.carver;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.NetherCaveWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class PermafrostCaveWorldCarver extends NetherCaveWorldCarver
{
    public PermafrostCaveWorldCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> p_i49927_1_)
    {
        super(p_i49927_1_);
        this.carvableBlocks = ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.GRASS_BLOCK, MubbleBlocks.PERMAROCK);
        this.carvableFluids = ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
    }
    
    @Override
    protected boolean carveBlock(IChunk chunkIn, BitSet carvingMask, Random p_222703_3_, BlockPos.MutableBlockPos p_222703_4_, BlockPos.MutableBlockPos p_222703_5_, BlockPos.MutableBlockPos p_222703_6_, int p_222703_7_, int p_222703_8_, int p_222703_9_, int p_222703_10_, int p_222703_11_, int p_222703_12_, int p_222703_13_, int p_222703_14_, AtomicBoolean p_222703_15_)
    {
    	int i = p_222703_12_ | p_222703_14_ << 4 | p_222703_13_ << 8;
    	if (carvingMask.get(i))
    	{
    		return false;
    	}
    	else
    	{
    		carvingMask.set(i);
    		p_222703_4_.setPos(p_222703_10_, p_222703_13_, p_222703_11_);
    		if (this.func_222706_a(chunkIn.getBlockState(p_222703_4_)))
    		{
    			BlockState blockstate;
    			if (p_222703_13_ <= 31)
    			{
    				blockstate = WATER.getBlockState();
    			}
    			else
    			{
    				blockstate = CAVE_AIR;
    			}
             chunkIn.setBlockState(p_222703_4_, blockstate, false);
             return true;
    		}
    		else
    		{
    			return false;
          }
       }
    }
}