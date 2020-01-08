package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;

public class PalmTree extends SaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random)
	{
		return MubbleFeatures.PALM_TREE.configure(new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PALM_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(2, 0)).build());
	}
}