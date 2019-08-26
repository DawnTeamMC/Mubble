package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PinkPressGardenTree extends BigTree
{	
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return MubbleFeatures.PINK_PRESS_GARDEN_TREE;
	}
	
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getBigTreeFeature(Random random)
	{
		return MubbleFeatures.MEGA_PINK_PRESS_GARDEN_TREE;
	}
}