package hugman.mubble.objects.world.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class TomatoFeature extends Feature<DefaultFeatureConfig>
{
	public TomatoFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> config)
	{
		super(config);
	}

	public boolean generate(IWorld worldIn, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random rand, BlockPos pos, DefaultFeatureConfig config)
	{
		for (int i = 0; i < 64; ++i)
		{
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if(worldIn.getBlockState(blockpos).getMaterial().isReplaceable() && worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS_BLOCK)
			{
				worldIn.setBlockState(blockpos, MubbleBlocks.TOMATOES.getDefaultState().with(Properties.AGE_7, worldIn.getRandom().nextInt(4)), 2);
				worldIn.setBlockState(blockpos.down(), Blocks.FARMLAND.getDefaultState().with(Properties.MOISTURE, worldIn.getRandom().nextInt(2) + 2), 2);
			}
		}
		return true;
	}
}