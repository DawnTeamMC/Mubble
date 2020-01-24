package hugman.mubble.objects.world.trees;

import java.util.Random;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;

public class AutumnOakTree extends SaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random, boolean canHaveBeeHive)
	{
		return random.nextInt(10) == 0 ? Feature.FANCY_TREE.configure(canHaveBeeHive ? MubbleFeatureConfigs.FANCY_AUTUMN_OAK_BEEHIVED_CONFIG : MubbleFeatureConfigs.FANCY_AUTUMN_OAK_TREE_CONFIG) : Feature.NORMAL_TREE.configure(canHaveBeeHive ? MubbleFeatureConfigs.AUTUMN_OAK_TREE_BEEHIVED_CONFIG : MubbleFeatureConfigs.AUTUMN_OAK_TREE_CONFIG);
	}
}