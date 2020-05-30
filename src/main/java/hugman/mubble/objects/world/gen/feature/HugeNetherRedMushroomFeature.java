package hugman.mubble.objects.world.gen.feature;

import com.mojang.serialization.Codec;
import com.sun.istack.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.HugeRedMushroomFeature;

import java.util.Random;

public class HugeNetherRedMushroomFeature extends HugeRedMushroomFeature
{
	public HugeNetherRedMushroomFeature(Codec<HugeMushroomFeatureConfig> codec)
	{
		super(codec);
	}

	@Override
	public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, HugeMushroomFeatureConfig hugeMushroomFeatureConfig)
	{
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		BlockPos blockPos2 = getStartPos(serverWorldAccess, blockPos);
		if (blockPos2 == null)
		{
			return false;
		}
		else
		{
			int i = this.getHeight(random);
			int j = serverWorldAccess.getDimensionHeight();
			if (blockPos2.getY() + i + 1 >= j)
			{
				return false;
			}
			serverWorldAccess.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 4);
			this.generateCap(serverWorldAccess, random, blockPos, i, mutable, hugeMushroomFeatureConfig);
			this.generateStem(serverWorldAccess, random, blockPos, hugeMushroomFeatureConfig, i, mutable);
			return true;
		}
	}

	@Nullable
	private static BlockPos.Mutable getStartPos(WorldAccess world, BlockPos pos)
	{
		BlockPos.Mutable mutable = pos.mutableCopy();
		for (int i = pos.getY(); i >= 1; --i)
		{
			mutable.setY(i);
			Block block2 = world.getBlockState(mutable.down()).getBlock();
			if (isNetherGround(block2))
			{
				return mutable;
			}
		}
		return null;
	}

	public static boolean isNetherGround(Block block)
	{
		return block == Blocks.NETHERRACK || block == Blocks.NETHER_QUARTZ_ORE || block == Blocks.SOUL_SAND || block == Blocks.SOUL_SOIL;
	}
}
