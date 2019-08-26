package hugman.mubble.objects.world.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class TomatoFeature extends Feature<NoFeatureConfig>
{
	public TomatoFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
	{
		super(config);
	}

	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		for (int i = 0; i < 64; ++i)
		{
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if(worldIn.getBlockState(blockpos).getMaterial().isReplaceable() && worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS_BLOCK)
			{
				worldIn.setBlockState(blockpos, MubbleBlocks.TOMATOES.getDefaultState().with(BlockStateProperties.AGE_0_7, worldIn.getRandom().nextInt(4)), 2);
				worldIn.setBlockState(blockpos.down(), Blocks.FARMLAND.getDefaultState().with(BlockStateProperties.MOISTURE_0_7, worldIn.getRandom().nextInt(2) + 2), 2);
			}
		}
		return true;
	}
}