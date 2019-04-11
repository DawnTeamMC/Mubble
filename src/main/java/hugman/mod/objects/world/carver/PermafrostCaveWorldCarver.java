package hugman.mod.objects.world.carver;

import java.util.BitSet;

import com.google.common.collect.ImmutableSet;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.carver.NetherCaveWorldCarver;

public class PermafrostCaveWorldCarver extends NetherCaveWorldCarver
{
	public PermafrostCaveWorldCarver()
	{
		this.terrainBlocks = ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.GRASS_BLOCK, MubbleBlocks.PERMAROCK);
		this.terrainFluids = ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
	}
	
	@Override
	protected boolean carveAtTarget(IWorld worldIn, long seed, int mainChunkX, int mainChunkZ, double xRange, double yRange, double zRange, double placementXZBound, double placementYBound, BitSet mask)
	{
		double d0 = (double)(mainChunkX * 16 + 8);
		double d1 = (double)(mainChunkZ * 16 + 8);
		if (!(xRange < d0 - 16.0D - placementXZBound * 2.0D) && !(zRange < d1 - 16.0D - placementXZBound * 2.0D) && !(xRange > d0 + 16.0D + placementXZBound * 2.0D) && !(zRange > d1 + 16.0D + placementXZBound * 2.0D))
		{
			int i = Math.max(MathHelper.floor(xRange - placementXZBound) - mainChunkX * 16 - 1, 0);
			int j = Math.min(MathHelper.floor(xRange + placementXZBound) - mainChunkX * 16 + 1, 16);
			int k = Math.max(MathHelper.floor(yRange - placementYBound) - 1, 1);
			int l = Math.min(MathHelper.floor(yRange + placementYBound) + 1, 120);
			int i1 = Math.max(MathHelper.floor(zRange - placementXZBound) - mainChunkZ * 16 - 1, 0);
			int j1 = Math.min(MathHelper.floor(zRange + placementXZBound) - mainChunkZ * 16 + 1, 16);
			if (this.doesAreaHaveFluids(worldIn, mainChunkX, mainChunkZ, i, j, k, l, i1, j1)) return false;
			else if (i <= j && k <= l && i1 <= j1)
			{
				boolean flag = false;

				for(int k1 = i; k1 < j; ++k1)
				{
					int l1 = k1 + mainChunkX * 16;
					double d2 = ((double)l1 + 0.5D - xRange) / placementXZBound;

					for(int i2 = i1; i2 < j1; ++i2)
					{
						int j2 = i2 + mainChunkZ * 16;
						double d3 = ((double)j2 + 0.5D - zRange) / placementXZBound;

						for(int k2 = l; k2 > k; --k2)
						{
							double d4 = ((double)(k2 - 1) + 0.5D - yRange) / placementYBound;
							if (d4 > -0.7D && d2 * d2 + d4 * d4 + d3 * d3 < 1.0D)
							{
								int l2 = k1 | i2 << 4 | k2 << 8;
								if (!mask.get(l2))
								{
									mask.set(l2);
									if (this.isTargetAllowed(worldIn.getBlockState(new BlockPos(l1, k2, j2))))
									{
										if (k2 <= 31) worldIn.setBlockState(new BlockPos(l1, k2, j2), WATER_FLUID.getBlockState(), 2);
										else worldIn.setBlockState(new BlockPos(l1, k2, j2), DEFAULT_CAVE_AIR, 2);
										flag = true;
									}
								}
							}
						}
					}
				}
				return flag;
			}
			else return false;
		}
		else return false;
	}
}