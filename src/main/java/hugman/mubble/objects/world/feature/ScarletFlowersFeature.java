package hugman.mubble.objects.world.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;

public class ScarletFlowersFeature extends FlowerFeature<DefaultFeatureConfig>
{	
	public ScarletFlowersFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> config)
	{
		super(config);
	}
	
	@Override
	public boolean method_23369(IWorld iWorld, BlockPos blockPos, DefaultFeatureConfig featureConfig)
	{
		return false;
	}

	@Override
	public int method_23370(DefaultFeatureConfig featureConfig)
	{
		return 0;
	}

	@Override
	public BlockPos method_23371(Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig)
	{
		return null;
	}
	
	@Override
	public BlockState getFlowerToPlace(Random random, BlockPos pos, DefaultFeatureConfig config)
	{
		return MubbleBlocks.SCARLET_ORCHID.getDefaultState();
	}
}