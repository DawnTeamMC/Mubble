package hugman.mod.objects.world.feature.tree.scarlet;

import java.util.Random;

import javax.annotation.Nullable;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.trees.AbstractBigTree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class ScarletTree extends AbstractBigTree
{
	Block log = MubbleBlocks.SCARLET_LOG;
	Block leaves = MubbleBlocks.SCARLET_LEAVES;
	Block sapling = MubbleBlocks.SCARLET_SAPLING;
	
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return (AbstractTreeFeature<NoFeatureConfig>)(random.nextInt(10) == 0 ? new ScarletTreeTallFeature(true) : new ScarletTreeFeature(true));
	}
	
	@Nullable
	protected AbstractTreeFeature<NoFeatureConfig> getBigTreeFeature(Random random)
	{
		return new ScarletTreeLargeFeature(true);
	}
}