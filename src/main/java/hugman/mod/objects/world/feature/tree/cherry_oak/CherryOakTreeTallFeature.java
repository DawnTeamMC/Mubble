package hugman.mod.objects.world.feature.tree.cherry_oak;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeTallFeature;

public class CherryOakTreeTallFeature extends TreeTallFeature
{
	public CherryOakTreeTallFeature(boolean notify)
	{
		super(notify, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.CHERRY_OAK_LEAVES, MubbleBlocks.CHERRY_OAK_SAPLING);
	}
}