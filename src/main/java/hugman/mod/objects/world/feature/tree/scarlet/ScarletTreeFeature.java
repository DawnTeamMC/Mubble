package hugman.mod.objects.world.feature.tree.scarlet;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeFeature;

public class ScarletTreeFeature extends TreeFeature
{
	public ScarletTreeFeature(boolean notify)
	{
		super(notify, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	}
}