package hugman.mubble.objects.world.surface_builder;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class PermafrostSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig>
{
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	private static final BlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	private static final BlockState BLUE_ICE = Blocks.BLUE_ICE.getDefaultState();
	private static final BlockState ICE = Blocks.ICE.getDefaultState();
	protected long field_205552_a;
	protected OctavesNoiseGenerator field_205553_b;
	
	public PermafrostSurfaceBuilder(Function<Dynamic<?>, ? extends SurfaceBuilderConfig> p_i51305_1_)
	{
		super(p_i51305_1_);
	}

	@Override
	public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config)
	{
		int i = seaLevel + 1;
		int j = x & 15;
		int k = z & 15;
		double d0 = 0.03125D;
		boolean flag = this.field_205553_b.func_205563_a((double)x * d0, (double)z * d0, 0.0D) + random.nextDouble() * 0.2D > 0.0D;
		boolean flag1 = this.field_205553_b.func_205563_a((double)x * d0, 109.0D, (double)z * d0) + random.nextDouble() * 0.2D > 0.0D;
		int l = (int)(noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		int i1 = -1;
		BlockState iblockstate = PERMAROCK;
		BlockState iblockstate1 = PERMAROCK;

		for(int j1 = 127; j1 >= 0; --j1) {
			blockpos$mutableblockpos.setPos(j, j1, k);
			BlockState iblockstate2 = chunkIn.getBlockState(blockpos$mutableblockpos);
			if (iblockstate2.getBlock() != null && iblockstate2.getBlock() != Blocks.AIR)
			{
				if (iblockstate2.getBlock() == defaultBlock.getBlock())
				{
					if (i1 == -1)
					{
						if (l <= 0)
						{
							iblockstate = CAVE_AIR;
							iblockstate1 = PERMAROCK;
						}
						else if (j1 >= i - 4 && j1 <= i + 1)
						{
							iblockstate = PERMAROCK;
							iblockstate1 = PERMAROCK;
							if (flag1)
							{
								iblockstate = BLUE_ICE;
								iblockstate1 = PERMAROCK;
							}

							if (flag)
							{
								iblockstate = ICE;
								iblockstate1 = ICE;
							}
						}
						if (j1 < i && (iblockstate == null)) iblockstate = defaultFluid;
						i1 = l;
						if (j1 >= i - 1) chunkIn.setBlockState(blockpos$mutableblockpos, iblockstate, false);
						else chunkIn.setBlockState(blockpos$mutableblockpos, iblockstate1, false);
					}
					else if (i1 > 0)
					{
						--i1;
						chunkIn.setBlockState(blockpos$mutableblockpos, iblockstate1, false);
					}
				}
			} 
			else i1 = -1;
		}
	}

	@Override
	public void setSeed(long seed)
	{
		if (this.field_205552_a != seed || this.field_205553_b == null) this.field_205553_b = new OctavesNoiseGenerator(new SharedSeedRandom(seed), 4);
		this.field_205552_a = seed;
	}
}