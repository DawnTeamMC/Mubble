package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class ScarletTree extends BigTree
{
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return random.nextInt(10) == 0 ? MubbleFeatures.TALL_SCARLET_TREE : MubbleFeatures.SCARLET_TREE;
	}
	
	@Nullable
	protected AbstractTreeFeature<NoFeatureConfig> getBigTreeFeature(Random random)
	{
		return MubbleFeatures.LARGE_SCARLET_TREE;
	}
}