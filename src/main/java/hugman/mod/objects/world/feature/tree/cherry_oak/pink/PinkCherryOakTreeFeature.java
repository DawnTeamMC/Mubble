package hugman.mod.objects.world.feature.tree.cherry_oak.pink;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.tree.template.TreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PinkCherryOakTreeFeature extends TreeFeature
{
	public PinkCherryOakTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	}
}