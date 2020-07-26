package com.hugman.mubble.object.block.sapling_generator;

import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class WhiteCherryOakSaplingGenerator extends SaplingGenerator {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive) {
		return random.nextInt(10) == 0 ? beeHive ? MubbleConfiguredFeatures.FANCY_WHITE_CHERRY_OAK_BEES_005 : MubbleConfiguredFeatures.FANCY_WHITE_CHERRY_OAK : beeHive ? MubbleConfiguredFeatures.WHITE_CHERRY_OAK_BEES_005 : MubbleConfiguredFeatures.WHITE_CHERRY_OAK;
	}
}