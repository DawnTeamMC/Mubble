package hugman.mod.objects.world.feature.tree.autumn_oak;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeFeature;
import net.minecraft.init.Blocks;

public class AutumnOakTreeFeature extends TreeFeature
{
	public AutumnOakTreeFeature(boolean notify)
	{
		super(notify, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	}
}