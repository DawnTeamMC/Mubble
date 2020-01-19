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
	protected boolean carveAtPoint(IChunk chunk, Function<BlockPos, Biome> p_225556_2_, BitSet mask, Random rand, BlockPos.Mutable mutable1, BlockPos.Mutable mutable2, BlockPos.Mutable mutable3, int p_225556_8_, int p_225556_9_, int p_225556_10_, int p_225556_11_, int p_225556_12_, int p_225556_13_, int p_225556_14_, int p_225556_15_, AtomicBoolean p_225556_16_)
	{
		int i = p_225556_13_ | p_225556_15_ << 4 | p_225556_14_ << 8;
		if (mask.get(i))
		{
			return false;
		}
		else
		{
			mask.set(i);
			mutable1.setPos(p_225556_11_, p_225556_14_, p_225556_12_);
			if (this.func_222706_a(chunk.getBlockState(mutable1)))
			{
				BlockState blockstate;
				if (p_225556_14_ <= 31)
				{
					blockstate = WATER.getBlockState();
				}
				else
				{
					blockstate = CAVE_AIR;
				}

				chunk.setBlockState(mutable1, blockstate, false);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}