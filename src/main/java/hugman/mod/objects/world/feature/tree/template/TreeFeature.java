package hugman.mod.objects.world.feature.tree.template;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;

public class TreeFeature extends AbstractTreeFeature<NoFeatureConfig>
{
	private final BlockState LOG;
	private final BlockState LEAVES;
	private final IPlantable SAPLING;

	public TreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify, Block log, Block leaves, Block sapling)
	{
		super(configFactory, notify);
		this.LOG = log.getDefaultState();
		this.LEAVES = leaves.getDefaultState();
		this.SAPLING = (IPlantable)sapling;
	}
	
	@Override
	protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox p_208519_5_)
	{
		int i = rand.nextInt(3) + 4;
		boolean flag = true;
		if (position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getMaxHeight())
		{
			for(int j = position.getY(); j <= position.getY() + 1 + i; ++j)
			{
				int k = 1;
	            if (j == position.getY()) k = 0;
	            if (j >= position.getY() + 1 + i - 2) k = 2;
	            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
	            for(int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
	            {
	            	for(int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
	            	{
	            		if (j >= 0 && j < worldIn.getMaxHeight())
	            		{
	            			if (!func_214587_a(worldIn, blockpos$mutableblockpos.setPos(l, j, i1))) flag = false;
	            		}
	            		else flag = false;
	            	}
	            }
			}

			if (!flag)
			{
				return false;
			}
			else if (isSoil(worldIn, position.down(), SAPLING) && position.getY() < worldIn.getMaxHeight() - i - 1)
			{
				this.setDirtAt(worldIn, position.down(), position);

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
								if (isAirOrLeaves(worldIn, blockpos) || func_214576_j(worldIn, blockpos))
								{
									this.setLogState(changedBlocks, worldIn, blockpos, this.LEAVES, p_208519_5_);
								}
							}
						}
					}
				}
				for(int i3 = 0; i3 < i; ++i3)
				{
					if (isAirOrLeaves(worldIn, position.up(i3)) || func_214576_j(worldIn, position.up(i3)))
					{
						this.setLogState(changedBlocks, worldIn, position.up(i3), this.LOG, p_208519_5_);
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