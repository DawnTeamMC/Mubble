package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;

public class WhiteCherryOakTree extends SaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl)
	{
		return random.nextInt(10) == 0 ? Feature.FANCY_TREE.configure(MubbleFeatureConfigs.FANCY_WHITE_CHERRY_OAK_TREE_CONFIG) : Feature.NORMAL_TREE.configure(MubbleFeatureConfigs.WHITE_CHERRY_OAK_TREE_CONFIG);
	}
}