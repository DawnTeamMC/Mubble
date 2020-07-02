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
	public boolean generate(ServerWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, ForestRockFeatureConfig config) {
		while(true) {
			label48:
			{
				if(pos.getY() > 3) {
					if(world.isAir(pos.down())) {
						break label48;
					}
					Block block = world.getBlockState(pos.down()).getBlock();
					if(!isEndStone(block)) {
						break label48;
					}
				}
				if(pos.getY() <= 3) {
					return false;
				}
				int i = config.startRadius;
				for(int j = 0; i >= 0 && j < 3; ++j) {
					int k = i + random.nextInt(2);
					int l = i + random.nextInt(8);
					int m = i + random.nextInt(2);
					float f = (float) (k + l + m) * 0.333F + 0.5F;
					Iterator var13 = BlockPos.iterate(pos.add(-k, -l, -m), pos.add(k, l, m)).iterator();
					while(var13.hasNext()) {
						BlockPos blockPos2 = (BlockPos) var13.next();
						if(blockPos2.getSquaredDistance(pos) <= (double) (f * f)) {
							world.setBlockState(blockPos2, config.state, 4);
						}
					}
					pos = pos.add(-(i + 1) + random.nextInt(2 + i * 2), 0 - random.nextInt(2), -(i + 1) + random.nextInt(2 + i * 2));
				}
				return true;
			}
			pos = pos.down();
		}
	}

	protected static boolean isEndStone(Block block) {
		return block == Blocks.END_STONE;
	}
}
