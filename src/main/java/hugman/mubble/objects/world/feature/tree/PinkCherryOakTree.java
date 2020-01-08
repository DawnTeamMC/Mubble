package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;

public class PinkCherryOakTree extends SaplingGenerator
{
	@Override
	protected ConfiguredFeature<BranchedTreeFeatureConfig, ?> createTreeFeature(Random random)
	{
		return random.nextInt(10) == 0 ? MubbleFeatures.TALL_PINK_CHERRY_OAK_TREE.configure(new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.CHERRY_OAK_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PINK_CHERRY_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build()) : MubbleFeatures.PINK_CHERRY_OAK_TREE.configure(new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.CHERRY_OAK_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PINK_CHERRY_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build());
	}
}