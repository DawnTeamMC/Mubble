package hugman.mubble.objects.block.sapling_generator;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class AutumnBirchSaplingGenerator extends SaplingGenerator {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive) {
		return random.nextInt(10) == 0 ? Feature.TREE.configure(beeHive ? MubbleFeatureConfigs.FANCY_AUTUMN_BIRCH_TREE_B1_CONFIG : MubbleFeatureConfigs.FANCY_AUTUMN_BIRCH_TREE) : Feature.TREE.configure(beeHive ? MubbleFeatureConfigs.AUTUMN_BIRCH_TREE_B1 : MubbleFeatureConfigs.AUTUMN_BIRCH_TREE);
	}
}