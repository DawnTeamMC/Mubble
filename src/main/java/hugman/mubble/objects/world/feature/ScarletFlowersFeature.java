package hugman.mubble.objects.world.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class ScarletFlowersFeature extends FlowersFeature
{	
	public ScarletFlowersFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
	{
		super(config);
	}

	public BlockState getRandomFlower(Random random, BlockPos pos)
	{
		return MubbleBlocks.SCARLET_ORCHID.getDefaultState();
	}
}