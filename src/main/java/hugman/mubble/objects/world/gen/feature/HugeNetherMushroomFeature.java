package hugman.mubble.objects.world.gen.feature;

import com.mojang.serialization.Codec;
import com.sun.istack.internal.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TwistingVinesFeature;
import net.minecraft.world.gen.feature.WeepingVinesFeature;

import java.util.Random;

public class HugeNetherMushroomFeature extends Feature<HugeNetherMushroomFeatureConfig>
{
	public HugeNetherMushroomFeature(Codec<HugeNetherMushroomFeatureConfig> codec)
	{
		super(codec);
	}

	public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos pos, HugeNetherMushroomFeatureConfig config)
	{
		int height = getHeight(random, config);
		BlockPos origin = getOrigin(world, pos, height, config);
		if (origin == null)
		{
			return false;
		}
		else
		{
			if (origin.getY() + height + 1 >= world.getDimensionHeight())
			{
				return false;
			}
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
			this.generateStem(world, random, origin, height, config);
			if (config.flatHat)
			{
				this.generateFlatHat(world, random, origin, height, config);
			}
			else
			{
				this.generateBubbleHat(world, random, origin, height, config);
			}
			return true;
		}
	}

	private void generateStem(WorldAccess world, Random random, BlockPos origin, int height, HugeNetherMushroomFeatureConfig config)
	{
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		BlockState blockState = config.stemState;
		for (int i = 0; i < height; ++i)
		{
			mutable.set(origin).move(config.upsideDown ? Direction.DOWN : Direction.UP, i);
			if (isReplaceable(world, mutable, true, true))
			{
				this.setBlockState(world, mutable, blockState);
			}
		}
	}

	protected void generateBubbleHat(WorldAccess world, Random random, BlockPos origin, int height, HugeNetherMushroomFeatureConfig config)
	{
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int i = height - 3; i <= height; ++i)
		{
			int j = i < height ? config.hatSize : config.hatSize - 1;
			int k = config.hatSize - 2;
			for (int l = -j; l <= j; ++l)
			{
				for (int m = -j; m <= j; ++m)
				{
					boolean bl = l == -j;
					boolean bl2 = l == j;
					boolean bl3 = m == -j;
					boolean bl4 = m == j;
					boolean bl5 = bl || bl2;
					boolean bl6 = bl3 || bl4;
					if (i >= height || bl5 != bl6)
					{
						mutable.set(origin, l, config.upsideDown ? -i : i, m);
						if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable))
						{
							boolean bl7 = i >= height - 1;
							boolean bl8 = i <= height - 3;
							boolean up = config.upsideDown ? bl8 : bl7;
							boolean down = config.upsideDown ? bl7 : bl8;
							BlockState state = config.hatState.with(MushroomBlock.UP, up).with(MushroomBlock.DOWN, down).with(MushroomBlock.WEST, l < -k).with(MushroomBlock.EAST, l > k).with(MushroomBlock.NORTH, m < -k).with(MushroomBlock.SOUTH, m > k);
							this.generateHatBlock(world, random, mutable, state, config);
						}
					}
				}
			}
		}
	}

	protected void generateFlatHat(WorldAccess world, Random random, BlockPos origin, int height, HugeNetherMushroomFeatureConfig config)
	{
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int i = config.hatSize;
		for (int j = -i; j <= i; ++j)
		{
			for (int k = -i; k <= i; ++k)
			{
				boolean bl = j == -i;
				boolean bl2 = j == i;
				boolean bl3 = k == -i;
				boolean bl4 = k == i;
				boolean bl5 = bl || bl2;
				boolean bl6 = bl3 || bl4;
				if (!bl5 || !bl6)
				{
					mutable.set(origin, j, config.upsideDown ? -height : height, k);
					if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable))
					{
						boolean bl7 = bl || bl6 && j == 1 - i;
						boolean bl8 = bl2 || bl6 && j == i - 1;
						boolean bl9 = bl3 || bl5 && k == 1 - i;
						boolean bl10 = bl4 || bl5 && k == i - 1;
						BlockState state = config.hatState.with(MushroomBlock.UP, !config.upsideDown).with(MushroomBlock.DOWN, config.upsideDown).with(MushroomBlock.WEST, bl7).with(MushroomBlock.EAST, bl8).with(MushroomBlock.NORTH, bl9).with(MushroomBlock.SOUTH, bl10);
						this.generateHatBlock(world, random, mutable, state, config);
					}
				}
			}
		}
	}

	private void generateHatBlock(WorldAccess world, Random random, BlockPos pos, BlockState hatState, HugeNetherMushroomFeatureConfig config)
	{
		if (random.nextFloat() < config.decorationChance)
		{
			this.setBlockState(world, pos, config.decorationState);
		}
		else
		{
			this.setBlockState(world, pos, hatState);
			if (random.nextFloat() < config.vineChance)
			{
				generateVines(pos, world, random, config);
			}
		}
	}

	private static void generateVines(BlockPos pos, WorldAccess worldAccess, Random random, HugeNetherMushroomFeatureConfig config)
	{
		BlockPos.Mutable mutable = pos.mutableCopy().move(config.upsideDown ? Direction.UP : Direction.DOWN);
		if (worldAccess.isAir(mutable))
		{
			int i = MathHelper.nextInt(random, 1, 5);
			if (random.nextInt(7) == 0)
			{
				i *= 2;
			}
			if (config.upsideDown)
			{
				TwistingVinesFeature.generateVineColumn(worldAccess, random, mutable, i, 23, 25);
			}
			else
			{
				WeepingVinesFeature.generateVineColumn(worldAccess, random, mutable, i, 23, 25);
			}
		}
	}

	protected int getHeight(Random random, HugeNetherMushroomFeatureConfig config)
	{
		int i = config.baseHeight + random.nextInt(config.randomHeight);
		if (random.nextInt(12) == 0)
		{
			i *= 2;
		}
		return i;
	}

	@Nullable
	private static BlockPos.Mutable getOrigin(WorldAccess world, BlockPos pos, int height, HugeNetherMushroomFeatureConfig config)
	{
		BlockPos.Mutable mutable = pos.mutableCopy();
		for (int i = pos.getY(); i >= 1; --i)
		{
			mutable.setY(i);
			if (isValidGround(world, mutable.offset(config.upsideDown ? Direction.UP : Direction.DOWN)))
			{
				BlockPos.Mutable mutable2 = mutable.mutableCopy();
				boolean allReplaceable = true;
				for (int j = 0; j <= height + 1; ++j)
				{
					mutable2.move(config.upsideDown ? Direction.DOWN : Direction.UP, j);
					if (!isReplaceable(world, mutable2, false, true))
					{
						allReplaceable = false;
						break;
					}
				}
				if (allReplaceable)
				{
					return mutable;
				}
			}
		}
		return null;
	}

	private static boolean isReplaceable(WorldAccess worldAccess, BlockPos blockPos, boolean canReplaceFluid, boolean canReplaceAllPlants)
	{
		return worldAccess.testBlockState(blockPos, (blockState) ->
		{
			Material material = blockState.getMaterial();
			return blockState.isAir() || canReplaceFluid && blockState.isOf(Blocks.WATER) || canReplaceFluid && blockState.isOf(Blocks.LAVA) || material == Material.REPLACEABLE_PLANT || canReplaceAllPlants && material == Material.PLANT;
		});
	}

	public static boolean isValidGround(WorldAccess worldAccess, BlockPos blockPos)
	{
		return worldAccess.testBlockState(blockPos, (blockState) ->
		{
			return blockState.isOf(Blocks.NETHERRACK) || blockState.isOf(Blocks.NETHER_QUARTZ_ORE) || blockState.isOf(Blocks.NETHER_GOLD_ORE);
		});
	}
}
