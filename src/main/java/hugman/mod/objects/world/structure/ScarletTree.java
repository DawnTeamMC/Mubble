package hugman.mod.objects.world.structure;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.block.BlockSapling;
import net.minecraft.block.trees.AbstractTree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;

public class ScarletTree extends AbstractTree
{
	@Nullable
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return new TreeFeature(true, 4, MubbleBlocks.SCARLET_LOG.getDefaultState(), MubbleBlocks.SCARLET_LEAVES.getDefaultState(), false).setSapling((BlockSapling)MubbleBlocks.SCARLET_SAPLING);
	}

	@Nullable
	protected AbstractTreeFeature<NoFeatureConfig> getTallTreeFeature(Random random)
	{
		return new TallScarletTreeFeature(true);
	}
	
	@Nullable
	protected AbstractTreeFeature<NoFeatureConfig> getLargeTreeFeature(Random random)
	{
		return new LargeScarletTreeFeature(true);
	}
}