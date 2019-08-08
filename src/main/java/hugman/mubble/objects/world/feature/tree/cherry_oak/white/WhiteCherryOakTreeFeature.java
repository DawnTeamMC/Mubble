package hugman.mubble.objects.world.feature.tree.cherry_oak.white;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.tree.template.TreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class WhiteCherryOakTreeFeature extends TreeFeature
{
	public WhiteCherryOakTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	}
}