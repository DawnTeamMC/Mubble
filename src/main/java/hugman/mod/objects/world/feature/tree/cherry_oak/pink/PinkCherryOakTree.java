package hugman.mod.objects.world.feature.tree.cherry_oak.pink;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.trees.AbstractTree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PinkCherryOakTree extends AbstractTree
{	
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return (AbstractTreeFeature<NoFeatureConfig>)(random.nextInt(10) == 0 ? new PinkCherryOakTreeTallFeature(true) : new PinkCherryOakTreeFeature(true));
	}
}