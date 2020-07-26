package com.hugman.mubble.object.block.sapling_generator;

import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class ScarletSaplingGenerator extends LargeTreeSaplingGenerator {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive) {
		return random.nextInt(10) == 0 ? MubbleConfiguredFeatures.FANCY_SCARLET_TREE : MubbleConfiguredFeatures.SCARLET_TREE;
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createLargeTreeFeature(Random random) {
		return MubbleConfiguredFeatures.HUGE_SCARLET_TREE;
	}
}