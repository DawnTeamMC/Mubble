package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.MegaTreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;

public class ScarletTree extends LargeTreeSaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl)
	{
		return random.nextInt(10) == 0 ? MubbleFeatures.TALL_SCARLET_TREE.configure(new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build()) : MubbleFeatures.SCARLET_TREE.configure(new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build());
	}
	
	@Override
	protected ConfiguredFeature<MegaTreeFeatureConfig, ?> createLargeTreeFeature(Random random)
	{
		return MubbleFeatures.LARGE_SCARLET_TREE.configure(new MegaTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState())).build());
	}
}