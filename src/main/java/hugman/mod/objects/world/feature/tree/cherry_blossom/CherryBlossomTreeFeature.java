package hugman.mod.objects.world.feature.tree.cherry_blossom;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeFeature;
import net.minecraft.init.Blocks;

public class CherryBlossomTreeFeature extends TreeFeature
{
	public CherryBlossomTreeFeature(boolean notify)
	{
		super(notify, Blocks.OAK_LOG, MubbleBlocks.CHERRY_BLOSSOM_LEAVES, MubbleBlocks.CHERRY_BLOSSOM_SAPLING);
	}
}