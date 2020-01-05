package hugman.mubble.objects.world.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SpringFeatureConfig;

public class PermafrostSpringFeature extends Feature<SpringFeatureConfig>
{
	private static final BlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
	
	public PermafrostSpringFeature(Function<Dynamic<?>, ? extends SpringFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	public boolean generate(IWorld worldIn, ChunkGenerator<? extends ChunkGeneratorConfig> chunkIn, Random rand, BlockPos pos, SpringFeatureConfig config)
	{
		if (worldIn.getBlockState(pos.up()) != PERMAROCK) return false;
		else if (!worldIn.isAir(pos) && worldIn.getBlockState(pos) != PERMAROCK) return false;
		else
		{
			int i = 0;
            int j = 0;
            if (worldIn.getBlockState(pos.west()) == PERMAROCK) ++i;
            if (worldIn.getBlockState(pos.east()) == PERMAROCK) ++i;
            if (worldIn.getBlockState(pos.north()) == PERMAROCK)  ++i;
            if (worldIn.getBlockState(pos.south()) == PERMAROCK) ++i;
            if (worldIn.getBlockState(pos.down()) == PERMAROCK) ++i;
            if (worldIn.isAir(pos.west())) ++j;
            if (worldIn.isAir(pos.east())) ++j;
            if (worldIn.isAir(pos.north())) ++j;
            if (worldIn.isAir(pos.south())) ++j;
            if (worldIn.isAir(pos.down())) ++j;
            if (!config.requiresBlockBelow && i == 4 && j == 1 || i == 5)
            {
            	worldIn.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
            	worldIn.getFluidTickScheduler().schedule(pos, Fluids.WATER, 0);
            }
            return true;
		}
	}
}