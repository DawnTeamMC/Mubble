package hugman.mod.objects.world.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HellLavaConfig;

public class PermafrostSpringFeature extends Feature<HellLavaConfig>
{
	private static final BlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	
	public PermafrostSpringFeature(Function<Dynamic<?>, ? extends HellLavaConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> chunkIn, Random rand, BlockPos pos, HellLavaConfig config)
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
            if (!config.insideRock && i == 4 && j == 1 || i == 5)
            {
            	worldIn.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
            	worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, 0);
            }
            return true;
		}
	}
}