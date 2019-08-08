package hugman.mubble.objects.world.feature.tree.autumn_oak;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class AutumnOakTree extends Tree
{	
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return (AbstractTreeFeature<NoFeatureConfig>)(random.nextInt(10) == 0 ? new AutumnOakTreeTallFeature(NoFeatureConfig::deserialize, true) : new AutumnOakTreeFeature(NoFeatureConfig::deserialize, true));
	}
}