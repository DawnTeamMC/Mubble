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
	public ScarletFlowersFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49876_1_)
	{
		super(p_i49876_1_);
	}

	public BlockState getRandomFlower(Random p_202355_1_, BlockPos p_202355_2_)
	{
		return MubbleBlocks.SCARLET_ORCHID.getDefaultState();
	}
}