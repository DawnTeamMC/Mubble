package hugman.mubble.objects.world.feature.tree.cherry_oak.white;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.tree.template.TreeTallFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class WhiteCherryOakTreeTallFeature extends TreeTallFeature
{
	public WhiteCherryOakTreeTallFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	}
}