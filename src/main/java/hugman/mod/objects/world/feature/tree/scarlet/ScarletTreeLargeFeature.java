package hugman.mod.objects.world.feature.tree.scarlet;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeLargeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class ScarletTreeLargeFeature extends TreeLargeFeature
{
	public ScarletTreeLargeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	}
}