package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.MegaTreeFeatureConfig;

public class RedPressGardenTree extends LargeTreeSaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random)
	{
		return MubbleFeatures.RED_PRESS_GARDEN_TREE.configure(MubbleFeatures.Config.RED_PRESS_GARDEN_TREE);
	}
	
	@Override
	protected ConfiguredFeature<MegaTreeFeatureConfig, ?> createLargeTreeFeature(Random random)
	{
		return MubbleFeatures.MEGA_RED_PRESS_GARDEN_TREE.configure(MubbleFeatures.Config.MEGA_RED_PRESS_GARDEN_TREE);
	}
}