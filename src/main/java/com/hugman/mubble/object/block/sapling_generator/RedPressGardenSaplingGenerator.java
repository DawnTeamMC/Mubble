package com.hugman.mubble.object.block.sapling_generator;

import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class RedPressGardenSaplingGenerator extends LargeTreeSaplingGenerator {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive) {
		return random.nextInt(10) == 0 ? MubbleConfiguredFeatures.FANCY_RED_PRESS_GARDEN_TREE : MubbleConfiguredFeatures.RED_PRESS_GARDEN_TREE;
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createLargeTreeFeature(Random random) {
		return MubbleConfiguredFeatures.MEGA_RED_PRESS_GARDEN_TREE;
	}
}