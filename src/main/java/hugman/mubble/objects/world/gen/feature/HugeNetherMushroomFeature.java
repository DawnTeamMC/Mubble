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
		int height = getHeight(random);
		BlockPos origin = getOrigin(world, pos, height);
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
			if (!config.flatHat)
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
			mutable.set(origin).move(Direction.UP, i);
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
			int j = i < height ? config.foliageRadius : config.foliageRadius - 1;
			int k = config.foliageRadius - 2;
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
						mutable.set(origin, l, i, m);
						if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable))
						{
							this.generateHatBlock(world, random, config, mutable, config.hatState.with(MushroomBlock.UP, i >= height - 1).with(MushroomBlock.WEST, l < -k).with(MushroomBlock.EAST, l > k).with(MushroomBlock.NORTH, m < -k).with(MushroomBlock.SOUTH, m > k), 0.2F, 0.1F);
						}
					}
				}
			}
		}
	}

	protected void generateFlatHat(WorldAccess world, Random random, BlockPos origin, int height, HugeNetherMushroomFeatureConfig config)
	{
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int i = height - 3; i <= height; ++i)
		{
			int j = i < height ? config.foliageRadius : config.foliageRadius - 1;
			int k = config.foliageRadius - 2;
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
						mutable.set(origin, l, i, m);
						if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable))
						{
							this.generateHatBlock(world, random, config, mutable, config.hatState.with(MushroomBlock.UP, i >= height - 1).with(MushroomBlock.DOWN, i <= height - 3).with(MushroomBlock.WEST, l < -k).with(MushroomBlock.EAST, l > k).with(MushroomBlock.NORTH, m < -k).with(MushroomBlock.SOUTH, m > k), 0.2F, 0.1F);
						}
					}
				}
			}
		}
	}

	private void generateHatBlock(WorldAccess world, Random random, HugeNetherMushroomFeatureConfig config, BlockPos pos, BlockState hatState, float decorationChance, float vineChance)
	{
		if (random.nextFloat() < decorationChance)
		{
			this.setBlockState(world, pos, config.decorationState);
		}
		else
		{
			this.setBlockState(world, pos, hatState);
			if (random.nextFloat() < vineChance)
			{
				generateVines(pos, world, random);
			}
		}
	}

	private static void generateVines(BlockPos pos, WorldAccess worldAccess, Random random)
	{
		BlockPos.Mutable mutable = pos.mutableCopy().move(Direction.DOWN);
		if (worldAccess.isAir(mutable))
		{
			int i = MathHelper.nextInt(random, 1, 5);
			if (random.nextInt(7) == 0)
			{
				i *= 2;
			}
			WeepingVinesFeature.generateVineColumn(worldAccess, random, mutable, i, 23, 25);
		}
	}

	protected int getHeight(Random random)
	{
		int i = random.nextInt(3) + 4;
		if (random.nextInt(12) == 0)
		{
			i *= 2;
		}
		return i;
	}

	@Nullable
	private static BlockPos.Mutable getOrigin(WorldAccess world, BlockPos pos, int height)
	{
		BlockPos.Mutable mutable = pos.mutableCopy();
		for (int i = pos.getY(); i >= 1; --i)
		{
			mutable.setY(i);
			if (isNetherGround(world, mutable.down()))
			{
				BlockPos.Mutable mutable2 = mutable.mutableCopy();
				boolean allReplaceable = true;
				for (int j = 0; j <= height + 1; ++j)
				{
					mutable2.move(Direction.UP, j);
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

	public static boolean isNetherGround(WorldAccess worldAccess, BlockPos blockPos)
	{
		return worldAccess.testBlockState(blockPos, (blockState) ->
		{
			return blockState.isOf(Blocks.NETHERRACK) || blockState.isOf(Blocks.NETHER_QUARTZ_ORE) || blockState.isOf(Blocks.NETHER_GOLD_ORE);
		});
	}
}
