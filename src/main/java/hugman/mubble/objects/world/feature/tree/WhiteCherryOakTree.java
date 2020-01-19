package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class WhiteCherryOakTree extends Tree
{	
	@Nullable
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random)
	{
	      return random.nextInt(10) == 0 ? Feature.FANCY_TREE.configure(MubbleFeatureConfigs.FANCY_WHITE_CHERRY_OAK_TREE_CONFIG) : Feature.NORMAL_TREE.configure(MubbleFeatureConfigs.WHITE_CHERRY_OAK_TREE_CONFIG);
	}
}