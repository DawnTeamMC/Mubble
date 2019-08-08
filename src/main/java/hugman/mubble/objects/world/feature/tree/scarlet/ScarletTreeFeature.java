package hugman.mubble.objects.world.feature.tree.scarlet;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.tree.template.TreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class ScarletTreeFeature extends TreeFeature
{
	public ScarletTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	}
}