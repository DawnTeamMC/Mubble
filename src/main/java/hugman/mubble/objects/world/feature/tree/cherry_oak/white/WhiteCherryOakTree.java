package hugman.mubble.objects.world.feature.tree.cherry_oak.white;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class WhiteCherryOakTree extends Tree
{	
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return (AbstractTreeFeature<NoFeatureConfig>)(random.nextInt(10) == 0 ? new WhiteCherryOakTreeTallFeature(NoFeatureConfig::deserialize, true) : new WhiteCherryOakTreeFeature(NoFeatureConfig::deserialize, true));
	}
}