package hugman.mubble.objects.world.feature.tree;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class WhiteCherryOakTree extends Tree
{	
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return random.nextInt(10) == 0 ? MubbleFeatures.TALL_WHITE_CHERRY_OAK_TREE : MubbleFeatures.WHITE_CHERRY_OAK_TREE;
	}
}