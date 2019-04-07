package hugman.mod.objects.world.feature;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.AbstractFlowersFeature;

public class ScarletFlowersFeature extends AbstractFlowersFeature
{
	public IBlockState getRandomFlower(Random p_202355_1_, BlockPos p_202355_2_)
	{
		return MubbleBlocks.SCARLET_ORCHID.getDefaultState();
	}
}