package hugman.mod.objects.world.feature.tree.cherry_oak.white;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeTallFeature;

public class WhiteCherryOakTreeTallFeature extends TreeTallFeature
{
	public WhiteCherryOakTreeTallFeature(boolean notify)
	{
		super(notify, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	}
}