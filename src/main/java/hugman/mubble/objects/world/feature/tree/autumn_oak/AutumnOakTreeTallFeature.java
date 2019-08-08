package hugman.mubble.objects.world.feature.tree.autumn_oak;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.tree.template.TreeTallFeature;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class AutumnOakTreeTallFeature extends TreeTallFeature
{
	public AutumnOakTreeTallFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	}
}