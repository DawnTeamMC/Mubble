package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class WhiteCherryOakTree extends SaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random)
	{
		return random.nextInt(10) == 0 ? MubbleFeatures.TALL_WHITE_CHERRY_OAK_TREE.configure(MubbleFeatures.Config.TALL_WHITE_CHERRY_OAK_TREE) : MubbleFeatures.WHITE_CHERRY_OAK_TREE.configure(MubbleFeatures.Config.WHITE_CHERRY_OAK_TREE);
	}
}