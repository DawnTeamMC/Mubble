package hugman.mubble.objects.world.feature.tree.template;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;

public class TreeFeature extends AbstractTreeFeature<BranchedTreeFeatureConfig>
{
	public TreeFeature(Function<Dynamic<?>, ? extends BranchedTreeFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected boolean generate(ModifiableTestableWorld worldIn, Random rand, BlockPos position, Set<BlockPos> changedBlocks, Set<BlockPos> leavesPositions, BlockBox box, BranchedTreeFeatureConfig config)
	{
		int i = rand.nextInt(3) + 4;
		boolean flag = true;
		if (position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE, position).getY())
		{
			for(int j = position.getY(); j <= position.getY() + 1 + i; ++j)
			{
				int k = 1;
	            if (j == position.getY())
	            {
	            	k = 0;
	            }
	            if (j >= position.getY() + 1 + i - 2)
	            {
	            	k = 2;
	            }
	            BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();
	            for(int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
	            {
	            	for(int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
	            	{
	            		if (j >= 0 && j < worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE, position).getY())
	            		{
	            			if (!canTreeReplace(worldIn, blockpos$mutableblockpos.set(l, j, i1)))
	            			{
	            				flag = false;
	            			}
	            		}
	            		else flag = false;
	            	}
	            }
			}

			if (!flag)
			{
				return false;
			}
			else if (isDirtOrGrass(worldIn, position.down()) && position.getY() < worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE, position).getY() - i - 1)
			{
				this.setToDirt(worldIn, position);

				for(int l2 = position.getY() - 3 + i; l2 <= position.getY() + i; ++l2)
				{
					int l3 = l2 - (position.getY() + i);
					int j4 = 1 - l3 / 2;
					for(int j1 = position.getX() - j4; j1 <= position.getX() + j4; ++j1)
					{
						int k1 = j1 - position.getX();
						for(int l1 = position.getZ() - j4; l1 <= position.getZ() + j4; ++l1)
						{
							int i2 = l1 - position.getZ();
							if (Math.abs(k1) != j4 || Math.abs(i2) != j4 || rand.nextInt(2) != 0 && l3 != 0)
							{
								BlockPos blockpos = new BlockPos(j1, l2, l1);
								if (isAirOrLeaves(worldIn, blockpos) || isReplaceablePlant(worldIn, blockpos))
								{
									this.setLeavesBlockState(worldIn, rand, blockpos, leavesPositions, box, config);
								}
							}
						}
					}
				}
				for(int i3 = 0; i3 < i; ++i3)
				{
					if (isAirOrLeaves(worldIn, position.up(i3)) || isReplaceablePlant(worldIn, position.up(i3)))
					{
						this.setLogBlockState(worldIn, rand, position.up(i3), changedBlocks, box, config);
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
}