package hugman.mubble.objects.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ForestRockFeatureConfig;

import java.util.Iterator;
import java.util.Random;

public class EndBoulderFeature extends Feature<ForestRockFeatureConfig> {
	public EndBoulderFeature(Codec<ForestRockFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, ForestRockFeatureConfig boulderFeatureConfig) {
		while(true) {
			label48:
			{
				if(blockPos.getY() > 3) {
					if(serverWorldAccess.isAir(blockPos.down())) {
						break label48;
					}
					Block block = serverWorldAccess.getBlockState(blockPos.down()).getBlock();
					if(!isEndStone(block)) {
						break label48;
					}
				}
				if(blockPos.getY() <= 3) {
					return false;
				}
				int i = boulderFeatureConfig.startRadius;
				for(int j = 0; i >= 0 && j < 3; ++j) {
					int k = i + random.nextInt(2);
					int l = i + random.nextInt(8);
					int m = i + random.nextInt(2);
					float f = (float) (k + l + m) * 0.333F + 0.5F;
					Iterator var13 = BlockPos.iterate(blockPos.add(-k, -l, -m), blockPos.add(k, l, m)).iterator();
					while(var13.hasNext()) {
						BlockPos blockPos2 = (BlockPos) var13.next();
						if(blockPos2.getSquaredDistance(blockPos) <= (double) (f * f)) {
							serverWorldAccess.setBlockState(blockPos2, boulderFeatureConfig.state, 4);
						}
					}
					blockPos = blockPos.add(-(i + 1) + random.nextInt(2 + i * 2), 0 - random.nextInt(2), -(i + 1) + random.nextInt(2 + i * 2));
				}
				return true;
			}
			blockPos = blockPos.down();
		}
	}

	protected static boolean isEndStone(Block block) {
		return block == Blocks.END_STONE;
	}
}
