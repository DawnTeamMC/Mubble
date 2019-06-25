package hugman.mod.objects.world.feature.tree.palm;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PalmTree extends Tree
{
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return new PalmTreeFeature(NoFeatureConfig::deserialize, true);
	}
}