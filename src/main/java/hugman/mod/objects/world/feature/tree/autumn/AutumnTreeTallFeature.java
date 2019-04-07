package hugman.mod.objects.world.feature.tree.autumn;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeTallFeature;
import net.minecraft.init.Blocks;

public class AutumnTreeTallFeature extends TreeTallFeature
{
	public AutumnTreeTallFeature(boolean notify)
	{
		super(notify, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_LEAVES, MubbleBlocks.AUTUMN_SAPLING);
	}
}