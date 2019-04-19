package hugman.mod.objects.world.feature;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HellLavaConfig;

public class PermafrostWaterFeature extends Feature<HellLavaConfig>
{
	private static final IBlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	public boolean func_212245_a(IWorld worldIn, IChunkGenerator<? extends IChunkGenSettings> chunkIn, Random rand, BlockPos pos, HellLavaConfig config)
	{
		if (worldIn.getBlockState(pos.up()) != PERMAROCK) return false;
		else if (!worldIn.isAirBlock(pos) && worldIn.getBlockState(pos) != PERMAROCK) return false;
		else
		{
			int i = 0;
            int j = 0;
            if (worldIn.getBlockState(pos.west()) == PERMAROCK) ++i;
            if (worldIn.getBlockState(pos.east()) == PERMAROCK) ++i;
            if (worldIn.getBlockState(pos.north()) == PERMAROCK)  ++i;
            if (worldIn.getBlockState(pos.south()) == PERMAROCK) ++i;
            if (worldIn.getBlockState(pos.down()) == PERMAROCK) ++i;
            if (worldIn.isAirBlock(pos.west())) ++j;
            if (worldIn.isAirBlock(pos.east())) ++j;
            if (worldIn.isAirBlock(pos.north())) ++j;
            if (worldIn.isAirBlock(pos.south())) ++j;
            if (worldIn.isAirBlock(pos.down())) ++j;
            if (!config.field_202437_a && i == 4 && j == 1 || i == 5)
            {
            	worldIn.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
            	worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, 0);
            }
            return true;
		}
	}
}