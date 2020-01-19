package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class PalmTree extends Tree
{
	@Nullable
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random)
	{
		return MubbleFeatures.PALM_TREE.configure(MubbleFeatureConfigs.PALM_TREE_CONFIG);
	}
}