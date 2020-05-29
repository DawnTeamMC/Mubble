package hugman.mubble.objects.world.sapling_generator;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class RedPressGardenSaplingGenerator extends LargeTreeSaplingGenerator
{
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive)
	{
		return random.nextInt(10) == 0 ? Feature.TREE.configure(MubbleFeatureConfigs.FANCY_RED_PRESS_GARDEN_TREE_CONFIG) : Feature.TREE.configure(MubbleFeatureConfigs.RED_PRESS_GARDEN_TREE_CONFIG);
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createLargeTreeFeature(Random random)
	{
		return Feature.TREE.configure(MubbleFeatureConfigs.MEGA_RED_PRESS_GARDEN_TREE_CONFIG);
	}
}