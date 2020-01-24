package hugman.mubble.objects.world.trees;

import java.util.Random;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class PalmTree extends SaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random, boolean canHaveBeeHive)
	{
		return MubbleFeatures.PALM_TREE.configure(MubbleFeatureConfigs.PALM_TREE_CONFIG);
	}
}