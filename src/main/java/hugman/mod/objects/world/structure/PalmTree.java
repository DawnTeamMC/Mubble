package hugman.mod.objects.world.structure;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.trees.AbstractTree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PalmTree extends AbstractTree
{
	@Nullable
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return new PalmTreeFeature(true);
	}
}