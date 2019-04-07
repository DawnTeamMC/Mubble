package hugman.mod.objects.world.feature.tree.autumn;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeFeature;
import net.minecraft.init.Blocks;

public class AutumnTreeFeature extends TreeFeature
{
	public AutumnTreeFeature(boolean notify)
	{
		super(notify, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_LEAVES, MubbleBlocks.AUTUMN_SAPLING);
	}
}