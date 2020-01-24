package hugman.mubble.objects.world.trees;

import java.util.Random;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MegaTreeFeatureConfig;

public class ScarletTree extends LargeTreeSaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random, boolean canHaveBeeHive)
	{
		return random.nextInt(10) == 0 ? Feature.FANCY_TREE.configure(MubbleFeatureConfigs.FANCY_SCARLET_TREE_CONFIG) : Feature.NORMAL_TREE.configure(MubbleFeatureConfigs.SCARLET_TREE_CONFIG);
	}
	
	@Override
	protected ConfiguredFeature<MegaTreeFeatureConfig, ?> createLargeTreeFeature(Random random)
	{
		return Feature.DARK_OAK_TREE.configure(MubbleFeatureConfigs.HUGE_SCARLET_TREE_CONFIG);
	}
}