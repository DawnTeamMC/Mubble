package hugman.mod.objects.world.surface_builder;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class PermafrostSurfaceBuilder implements ISurfaceBuilder<SurfaceBuilderConfig>
{
	private static final IBlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	private static final IBlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	private static final IBlockState BLUE_ICE = Blocks.BLUE_ICE.getDefaultState();
	private static final IBlockState ICE = Blocks.ICE.getDefaultState();
	protected long field_205552_a;
	protected NoiseGeneratorOctaves field_205553_b;

	@Override
	public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, IBlockState defaultBlock, IBlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config)
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
		IBlockState iblockstate = PERMAROCK;
		IBlockState iblockstate1 = PERMAROCK;

		for(int j1 = 127; j1 >= 0; --j1) {
			blockpos$mutableblockpos.setPos(j, j1, k);
			IBlockState iblockstate2 = chunkIn.getBlockState(blockpos$mutableblockpos);
			if (iblockstate2.getBlock() != null && !iblockstate2.isAir())
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
						if (j1 < i && (iblockstate == null || iblockstate.isAir())) iblockstate = defaultFluid;
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
		if (this.field_205552_a != seed || this.field_205553_b == null) this.field_205553_b = new NoiseGeneratorOctaves(new SharedSeedRandom(seed), 4);
		this.field_205552_a = seed;
	}
}