package com.hugman.mubble.object.world.gen.foliage;

import com.hugman.mubble.init.world.MubbleFoliagePlacerTypes;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

public class CatFoliagePlacer extends FoliagePlacer {
	public static final Codec<CatFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
		return fillFoliagePlacerFields(instance).and(Codec.intRange(0, 16).fieldOf("height").forGetter((config) -> {
			return config.height;
		})).apply(instance, CatFoliagePlacer::new);
	});
	protected final int height;

	public CatFoliagePlacer(UniformIntDistribution radius, UniformIntDistribution offset, int height) {
		super(radius, offset);
		this.height = height;
	}

	@Override
	protected FoliagePlacerType<?> getType() {
		return MubbleFoliagePlacerTypes.CAT_FOLIAGE_PLACER;
	}

	@Override
	protected void generate(ModifiableTestableWorld world, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, Set<BlockPos> leaves, int offset, BlockBox box) {
		for(int i = offset; i >= offset - foliageHeight; --i) {
			int j = Math.max(radius + treeNode.getFoliageRadius() - 1 - i / 2, 0);
			this.generateSquare(world, random, config, treeNode.getCenter(), j, leaves, i, treeNode.isGiantTrunk(), box);
		}
		if(random.nextBoolean()) {
			this.generateBlock(world, random, config, treeNode.getCenter().up(1).north(1), leaves, 0, treeNode.isGiantTrunk(), box);
			this.generateBlock(world, random, config, treeNode.getCenter().up(1).south(1), leaves, 0, treeNode.isGiantTrunk(), box);
		}
		else {
			this.generateBlock(world, random, config, treeNode.getCenter().up(1).west(1), leaves, 0, treeNode.isGiantTrunk(), box);
			this.generateBlock(world, random, config, treeNode.getCenter().up(1).east(1), leaves, 0, treeNode.isGiantTrunk(), box);
		}
	}

	protected void generateBlock(ModifiableTestableWorld world, Random random, TreeFeatureConfig config, BlockPos pos, Set<BlockPos> leaves, int y, boolean giantTrunk, BlockBox box) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if (!this.isPositionInvalid(random, 0, y, 0, 1, giantTrunk)) {
			mutable.set(pos, 0, y, 0);
			if (TreeFeature.canReplace(world, mutable)) {
				world.setBlockState(mutable, config.leavesProvider.getBlockState(random, mutable), 19);
				box.encompass(new BlockBox(mutable, mutable));
				leaves.add(mutable.toImmutable());
			}
		}
	}

	@Override
	public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
		return this.height;
	}

	@Override
	protected boolean isInvalidForLeaves(Random random, int baseHeight, int dx, int y, int dz, boolean giantTrunk) {
		return baseHeight == dz && y == dz && (random.nextInt(2) == 0 || dx == 0);
	}
}
