package hugman.mubble.objects.world.feature.tree.template;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;

public class TallTreeFeature extends AbstractTreeFeature<BranchedTreeFeatureConfig>
{
	public TallTreeFeature(Function<Dynamic<?>, ? extends BranchedTreeFeatureConfig> configFactory) 
	{
		super(configFactory);
	}

	private void crossSection(ModifiableTestableWorld world, Random random, BlockPos p_208529_2_, float p_208529_3_, BlockBox box, Set<BlockPos> p_208529_5_, BranchedTreeFeatureConfig config)
	{
		int i = (int) ((double) p_208529_3_ + 0.618D);

		for (int j = -i; j <= i; ++j)
		{
			for (int k = -i; k <= i; ++k)
			{
				if (Math.pow((double) Math.abs(j) + 0.5D, 2.0D) + Math.pow((double) Math.abs(k) + 0.5D, 2.0D) <= (double) (p_208529_3_ * p_208529_3_))
				{
					BlockPos blockpos = p_208529_2_.add(j, 0, k);
					if (isAirOrLeaves(world, blockpos))
					{
						this.setLeavesBlockState(world, random, blockpos, p_208529_5_, box, config);
					}
				}
			}
		}

	}

	private float treeShape(int p_208527_1_, int p_208527_2_)
	{
		if ((float) p_208527_2_ < (float) p_208527_1_ * 0.3F)
		{
			return -1.0F;
		}
		else
		{
			float f = (float) p_208527_1_ / 2.0F;
			float f1 = f - (float) p_208527_2_;
			float f2 = MathHelper.sqrt(f * f - f1 * f1);
			if (f1 == 0.0F)
			{
				f2 = f;
			} else if (Math.abs(f1) >= f) 
			{
				return 0.0F;
			}

			return f2 * 0.5F;
		}
	}

	private float foliageShape(int y)
	{
		if (y >= 0 && y < 5)
		{
			return y != 0 && y != 4 ? 3.0F : 2.0F;
		}
		else
		{
			return -1.0F;
		}
	}

	private void foliageCluster(ModifiableTestableWorld p_202393_1_, Random random, BlockPos p_202393_2_, BlockBox p_202393_3_, Set<BlockPos> p_202393_4_, BranchedTreeFeatureConfig config)
	{
		for (int i = 0; i < 5; ++i)
		{
			this.crossSection(p_202393_1_, random, p_202393_2_.up(i), this.foliageShape(i), p_202393_3_, p_202393_4_, config);
		}

	}

	private int makeLimb(Set<BlockPos> p_208523_1_, ModifiableTestableWorld world, Random random, BlockPos p_208523_3_, BlockPos p_208523_4_, boolean p_208523_5_, BlockBox box, BranchedTreeFeatureConfig config)
	{
		if (!p_208523_5_ && Objects.equals(p_208523_3_, p_208523_4_))
		{
			return -1;
		}
		else
		{
			BlockPos blockpos = p_208523_4_.add(-p_208523_3_.getX(), -p_208523_3_.getY(), -p_208523_3_.getZ());
			int i = this.getGreatestDistance(blockpos);
			float f = (float) blockpos.getX() / (float) i;
			float f1 = (float) blockpos.getY() / (float) i;
			float f2 = (float) blockpos.getZ() / (float) i;

			for (int j = 0; j <= i; ++j)
			{
				BlockPos blockpos1 = p_208523_3_.add((double) (0.5F + (float) j * f), (double) (0.5F + (float) j * f1), (double) (0.5F + (float) j * f2));
				if (p_208523_5_)
				{
					this.setLogBlockState(world, random, blockpos1, p_208523_1_, box, config);
				}
				else if (!canTreeReplace(world, blockpos1))
				{
					return j;
				}
			}
			return -1;
		}
	}

	private int getGreatestDistance(BlockPos posIn) 
	{
		int i = MathHelper.abs(posIn.getX());
		int j = MathHelper.abs(posIn.getY());
		int k = MathHelper.abs(posIn.getZ());
		if (k > i && k > j)
		{
			return k;
		}
		else
		{
			return j > i ? j : i;
		}
	}

	private void makeFoliage(ModifiableTestableWorld p_208525_1_, Random random, int p_208525_2_, BlockPos p_208525_3_, List<TallTreeFeature.FoliageCoordinates> p_208525_4_, BlockBox p_208525_5_, Set<BlockPos> p_208525_6_, BranchedTreeFeatureConfig config)
	{
		for (TallTreeFeature.FoliageCoordinates TreeTallFeature$foliagecoordinates : p_208525_4_)
		{
			if (this.trimBranches(p_208525_2_,
					TreeTallFeature$foliagecoordinates.getBranchBase() - p_208525_3_.getY()))
			{
				this.foliageCluster(p_208525_1_, random, TreeTallFeature$foliagecoordinates, p_208525_5_, p_208525_6_, config);
			}
		}

	}

	private boolean trimBranches(int p_208522_1_, int p_208522_2_)
	{
		return (double) p_208522_2_ >= (double) p_208522_1_ * 0.2D;
	}

	private void makeTrunk(Set<BlockPos> p_208526_1_, ModifiableTestableWorld world, Random random, BlockPos p_208526_3_, int p_208526_4_, BlockBox p_208526_5_, BranchedTreeFeatureConfig config)
	{
		this.makeLimb(p_208526_1_, world, random, p_208526_3_, p_208526_3_.up(p_208526_4_), true, p_208526_5_, config);
	}

	private void makeBranches(Set<BlockPos> p_208524_1_, ModifiableTestableWorld world, Random random, int p_208524_3_, BlockPos p_208524_4_, List<TallTreeFeature.FoliageCoordinates> p_208524_5_, BlockBox p_208524_6_, BranchedTreeFeatureConfig config)
	{
		for (TallTreeFeature.FoliageCoordinates TreeTallFeature$foliagecoordinates : p_208524_5_)
		{
			int i = TreeTallFeature$foliagecoordinates.getBranchBase();
			BlockPos blockpos = new BlockPos(p_208524_4_.getX(), i, p_208524_4_.getZ());
			if (!blockpos.equals(TreeTallFeature$foliagecoordinates) && this.trimBranches(p_208524_3_, i - p_208524_4_.getY()))
			{
				this.makeLimb(p_208524_1_, world, random, blockpos, TreeTallFeature$foliagecoordinates, true, p_208524_6_, config);
			}
		}

	}
	
	@Override
	protected boolean generate(ModifiableTestableWorld worldIn, Random rand, BlockPos position, Set<BlockPos> changedBlocks, Set<BlockPos> leavesPositions, BlockBox box, BranchedTreeFeatureConfig config)
	{
		Random random = new Random(rand.nextLong());
		int i = this.checkLocation(changedBlocks, worldIn, rand, position, 5 + random.nextInt(12), box, config);
		if (i == -1)
		{
			return false;
		}
		else
		{
			this.setToDirt(worldIn, position);
			int j = (int) ((double) i * 0.618D);
			if (j >= i)
			{
				j = i - 1;
			}

			int k = (int) (1.382D + Math.pow(1.0D * (double) i / 13.0D, 2.0D));
			if (k < 1)
			{
				k = 1;
			}

			int l = position.getY() + j;
			int i1 = i - 5;
			List<TallTreeFeature.FoliageCoordinates> list = Lists.newArrayList();
			list.add(new TallTreeFeature.FoliageCoordinates(position.up(i1), l));

			for (; i1 >= 0; --i1)
			{
				float f = this.treeShape(i, i1);
				if (!(f < 0.0F))
				{
					for (int j1 = 0; j1 < k; ++j1)
					{
						double d2 = 1.0D * (double) f * ((double) random.nextFloat() + 0.328D);
						double d3 = (double) (random.nextFloat() * 2.0F) * Math.PI;
						double d4 = d2 * Math.sin(d3) + 0.5D;
						double d5 = d2 * Math.cos(d3) + 0.5D;
						BlockPos blockpos = position.add(d4, (double) (i1 - 1), d5);
						BlockPos blockpos1 = blockpos.up(5);
						if (this.makeLimb(changedBlocks, worldIn, rand, blockpos, blockpos1, false, box, config) == -1) {
							int k1 = position.getX() - blockpos.getX();
							int l1 = position.getZ() - blockpos.getZ();
							double d6 = (double) blockpos.getY() - Math.sqrt((double) (k1 * k1 + l1 * l1)) * 0.381D;
							int i2 = d6 > (double) l ? l : (int) d6;
							BlockPos blockpos2 = new BlockPos(position.getX(), i2, position.getZ());
							if (this.makeLimb(changedBlocks, worldIn, rand, blockpos2, blockpos, false, box, config) == -1)
							{
								list.add(new TallTreeFeature.FoliageCoordinates(blockpos, blockpos2.getY()));
							}
						}
					}
				}
			}

			this.makeFoliage(worldIn, rand, i, position, list, box, changedBlocks, config);
			this.makeTrunk(changedBlocks, worldIn, rand, position, j, box, config);
			this.makeBranches(changedBlocks, worldIn, rand, i, position, list, box, config);
			return true;
		}
	}

	private int checkLocation(Set<BlockPos> p_208528_1_, ModifiableTestableWorld world, Random random, BlockPos p_208528_3_, int p_208528_4_, BlockBox box, BranchedTreeFeatureConfig config)
	{
		if (!isDirtOrGrass(world, p_208528_3_.down()))
		{
			return -1;
		}
		else
		{
			int i = this.makeLimb(p_208528_1_, world, random, p_208528_3_, p_208528_3_.up(p_208528_4_ - 1), false, box, config);
			if (i == -1)
			{
				return p_208528_4_;
			}
			else
			{
				return i < 6 ? -1 : i;
			}
		}
	}

	static class FoliageCoordinates extends BlockPos 
	{
		private final int branchBase;

		public FoliageCoordinates(BlockPos pos, int p_i45635_2_)
		{
			super(pos.getX(), pos.getY(), pos.getZ());
			this.branchBase = p_i45635_2_;
		}

		public int getBranchBase()
		{
			return this.branchBase;
		}
	}
}