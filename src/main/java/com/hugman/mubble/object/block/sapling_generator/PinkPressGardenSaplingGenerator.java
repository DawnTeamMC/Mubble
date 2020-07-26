package com.hugman.mubble.object.block.sapling_generator;

import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class PinkPressGardenSaplingGenerator extends LargeTreeSaplingGenerator {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive) {
		return random.nextInt(10) == 0 ? MubbleConfiguredFeatures.FANCY_PINK_PRESS_GARDEN_TREE : MubbleConfiguredFeatures.PINK_PRESS_GARDEN_TREE;
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createLargeTreeFeature(Random random) {
		return MubbleConfiguredFeatures.MEGA_PINK_PRESS_GARDEN_TREE;
	}
}