package hugman.mubble.object.block.sapling_generator;

import hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class ScarletSaplingGenerator extends LargeTreeSaplingGenerator {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive) {
		return random.nextInt(10) == 0 ? Feature.TREE.configure(MubbleConfiguredFeatures.FANCY_SCARLET_TREE) : Feature.TREE.configure(MubbleConfiguredFeatures.SCARLET_TREE);
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createLargeTreeFeature(Random random) {
		return Feature.TREE.configure(MubbleConfiguredFeatures.HUGE_SCARLET_TREE);
	}
}