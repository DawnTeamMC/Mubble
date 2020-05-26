package hugman.mubble.objects.world.feature;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;

public class PalmTreeFeature extends AbstractTreeFeature<BranchedTreeFeatureConfig>
{	
	public PalmTreeFeature(Function<Dynamic<?>, ? extends BranchedTreeFeatureConfig> configFactory)
	{
		super(configFactory);
	}

	@Override
	public boolean generate(ModifiableTestableWorld world, Random rand, BlockPos position, Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, BlockBox boundingBox, BranchedTreeFeatureConfig config)
	{
		int i = rand.nextInt(config.heightRandA) + config.baseHeight;
		boolean flag = true;
		if(position.getY() >= 1)
		{
			for(int j = position.getY(); j <= position.getY() + 1 + i; ++j)
			{
				int k = 1;
	            if(j == position.getY())
	            {
	            	k = 0;
	            }
	            if(j >= position.getY() + 1 + i - 2)
	            {
	            	k = 2;
	            }
	            BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();
	            for(int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
	            {
	            	for(int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
	            	{
	            		if (j >= 0)
	            		{
	            			if (!canTreeReplace(world, blockpos$mutableblockpos.set(l, j, i1)))
	            			{
	            				flag = false;
	            			}
	            		}
	            		else flag = false;
	            	}
	            }
			}
			
			if(!flag)
	        {
				return false;
	        }
			else if(isNaturalDirtOrGrass(world, position.down()) || isSand(world, position.down()))
			{
				BlockPos pos = position.add(0, i - 1, 0);
		 		setToDirt(world, position.down());
		 		setLeavesBlockState(world, rand, pos.add(0, 1, 0), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(1, 1, 0), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-1, 1, 0), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(0, 1, 1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(0, 1, -1), changedLeaves, boundingBox, config);

		 		setLeavesBlockState(world, rand, pos.add(1, 0, 0), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-1, 0, 0), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(0, 0, 1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(0, 0, -1), changedLeaves, boundingBox, config);
		 		
		 		setLeavesBlockState(world, rand, pos.add(1, 0, 1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(1, 0, -1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-1, 0, 1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-1, 0, -1), changedLeaves, boundingBox, config);

		 		setLeavesBlockState(world, rand, pos.add(2, 0, 0), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(2, 0, 1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(2, 0, -1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-2, 0, 0), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-2, 0, 1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-2, 0, -1), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(0, 0, 2), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(1, 0, 2), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-1, 0, 2), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(0, 0, -2), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(1, 0, -2), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-1, 0, -2), changedLeaves, boundingBox, config);

		 		for (int i1 = -2; i1 <= 2; i1++)
		 		{
			 		setLeavesBlockState(world, rand, pos.add(2, -1, i1), changedLeaves, boundingBox, config);
			 		setLeavesBlockState(world, rand, pos.add(-2, -1, i1), changedLeaves, boundingBox, config);
			 		setLeavesBlockState(world, rand, pos.add(i1, -1, 2), changedLeaves, boundingBox, config);
			 		setLeavesBlockState(world, rand, pos.add(i1, -1, -2), changedLeaves, boundingBox, config);
		 		}
		 		setLeavesBlockState(world, rand, pos.add(2, -2, 2), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(2, -2, -2), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-2, -2, 2), changedLeaves, boundingBox, config);
		 		setLeavesBlockState(world, rand, pos.add(-2, -2, -2), changedLeaves, boundingBox, config);

		 		for (int i2 = -3; i2 <= -1; i2++)
		 		{
			 		setLeavesBlockState(world, rand, pos.add(3, i2, 0), changedLeaves, boundingBox, config);
			 		setLeavesBlockState(world, rand, pos.add(-3, i2, 0), changedLeaves, boundingBox, config);
			 		setLeavesBlockState(world, rand, pos.add(0, i2, 3), changedLeaves, boundingBox, config);
			 		setLeavesBlockState(world, rand, pos.add(0, i2, -3), changedLeaves, boundingBox, config);
		 		}
			 	for(int i3 = 0; i3 < i; i3++)
			 	{
			 		if(isAirOrLeaves(world, position.up(i3)) || isReplaceablePlant(world, position.up(i3)))
			 		{
			 			setLogBlockState(world, rand, position.up(i3), changedLogs, boundingBox, config);
			 		}
			 	}
			}
		}
		else
		{
			return false;
		}
		return flag;
	}
	
	private boolean isSand(TestableWorld world, BlockPos pos)
	{
		return world.testBlockState(pos, (state) ->
			{
				return state.matches(BlockTags.SAND);
			});
	}
}