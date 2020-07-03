package hugman.mubble.object.block.sapling_generator;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class ScarletSaplingGenerator extends LargeTreeSaplingGenerator {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive) {
		return random.nextInt(10) == 0 ? Feature.TREE.configure(MubbleFeatureConfigs.FANCY_SCARLET_TREE) : Feature.TREE.configure(MubbleFeatureConfigs.SCARLET_TREE);
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createLargeTreeFeature(Random random) {
		return Feature.TREE.configure(MubbleFeatureConfigs.HUGE_SCARLET_TREE);
	}
}