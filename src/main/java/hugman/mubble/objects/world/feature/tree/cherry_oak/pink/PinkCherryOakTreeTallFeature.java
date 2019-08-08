package hugman.mubble.objects.world.feature.tree.cherry_oak.pink;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.tree.template.TreeTallFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PinkCherryOakTreeTallFeature extends TreeTallFeature
{
	public PinkCherryOakTreeTallFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	}
}