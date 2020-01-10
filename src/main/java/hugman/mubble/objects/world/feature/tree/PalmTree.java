package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class PalmTree extends SaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random)
	{
		return MubbleFeatures.PALM_TREE.configure(MubbleFeatures.Config.PALM_TREE);
	}
}