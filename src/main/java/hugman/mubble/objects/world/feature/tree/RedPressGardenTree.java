package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class RedPressGardenTree extends BigTree
{
	@Nullable
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random rand)
	{
	      return rand.nextInt(10) == 0 ? Feature.FANCY_TREE.configure(MubbleFeatureConfigs.FANCY_RED_PRESS_GARDEN_TREE_CONFIG) : Feature.NORMAL_TREE.configure(MubbleFeatureConfigs.RED_PRESS_GARDEN_TREE_CONFIG);
	}
	
	@Nullable
	@Override
	protected ConfiguredFeature<HugeTreeFeatureConfig, ?> createLargeTreeFeature(Random rand)
	{
		return Feature.MEGA_JUNGLE_TREE.configure(MubbleFeatureConfigs.MEGA_RED_PRESS_GARDEN_TREE_CONFIG);
	}
}