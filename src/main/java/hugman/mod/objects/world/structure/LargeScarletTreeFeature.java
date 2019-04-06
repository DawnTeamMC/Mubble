package hugman.mod.objects.world.structure;

import java.util.Random;
import java.util.Set;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class LargeScarletTreeFeature extends AbstractTreeFeature<NoFeatureConfig>
{
	private static final IBlockState SCARLET_LOG = MubbleBlocks.SCARLET_LOG.getDefaultState();
	private static final IBlockState SCARLET_LEAVES = MubbleBlocks.SCARLET_LEAVES.getDefaultState();

	public LargeScarletTreeFeature(boolean notify)
	{
		super(notify);
	}

	@Override
	public boolean place(Set<BlockPos> changedBlocks, IWorld worldIn, Random rand, BlockPos position)
	{
		int i = rand.nextInt(3) + rand.nextInt(2) + 6;
		int j = position.getX();
		int k = position.getY();
		int l = position.getZ();
		if (k >= 1 && k + i + 1 < 256)
		{
			BlockPos blockpos = position.down();
			boolean isSoil = worldIn.getBlockState(blockpos).canSustainPlant(worldIn, blockpos, net.minecraft.util.EnumFacing.UP, (BlockSapling)MubbleBlocks.SCARLET_SAPLING);
			if (!isSoil) return false;
			else if (!this.placeTreeOfHeight(worldIn, position, i)) return false;
			else
			{
				this.setDirtAt(worldIn, blockpos, blockpos.up());
				this.setDirtAt(worldIn, blockpos.east(), blockpos.up());
				this.setDirtAt(worldIn, blockpos.south(), blockpos.up());
				this.setDirtAt(worldIn, blockpos.south().east(), blockpos.up());
				EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
				int i1 = i - rand.nextInt(4);
				int j1 = 2 - rand.nextInt(3);
				int k1 = j;
				int l1 = l;
				int i2 = k + i - 1;

				for(int j2 = 0; j2 < i; ++j2)
				{
					if (j2 >= i1 && j1 > 0)
					{
						k1 += enumfacing.getXOffset();
						l1 += enumfacing.getZOffset();
						--j1;
					}

					int k2 = k + j2;
					BlockPos blockpos1 = new BlockPos(k1, k2, l1);
					IBlockState iblockstate = worldIn.getBlockState(blockpos1);
					if (iblockstate.isAir(worldIn, blockpos1) || iblockstate.isIn(BlockTags.LEAVES))
					{
						this.func_208533_a(changedBlocks, worldIn, blockpos1);
						this.func_208533_a(changedBlocks, worldIn, blockpos1.east());
						this.func_208533_a(changedBlocks, worldIn, blockpos1.south());
						this.func_208533_a(changedBlocks, worldIn, blockpos1.east().south());
					}
				}

				for(int i3 = -2; i3 <= 0; ++i3)
				{
					for(int l3 = -2; l3 <= 0; ++l3)
					{
						int k4 = -1;
						this.func_202414_a(worldIn, k1 + i3, i2 + k4, l1 + l3);
						this.func_202414_a(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3);
						this.func_202414_a(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3);
						this.func_202414_a(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);
						if ((i3 > -2 || l3 > -1) && (i3 != -1 || l3 != -2))
						{
							k4 = 1;
							this.func_202414_a(worldIn, k1 + i3, i2 + k4, l1 + l3);
							this.func_202414_a(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3);
							this.func_202414_a(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3);
							this.func_202414_a(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);
						}
					}
				}

				if (rand.nextBoolean())
				{
					this.func_202414_a(worldIn, k1, i2 + 2, l1);
					this.func_202414_a(worldIn, k1 + 1, i2 + 2, l1);
					this.func_202414_a(worldIn, k1 + 1, i2 + 2, l1 + 1);
					this.func_202414_a(worldIn, k1, i2 + 2, l1 + 1);
				}

				for(int j3 = -3; j3 <= 4; ++j3) for(int i4 = -3; i4 <= 4; ++i4) if ((j3 != -3 || i4 != -3) && (j3 != -3 || i4 != 4) && (j3 != 4 || i4 != -3) && (j3 != 4 || i4 != 4) && (Math.abs(j3) < 3 || Math.abs(i4) < 3)) this.func_202414_a(worldIn, k1 + j3, i2, l1 + i4);
				for(int k3 = -1; k3 <= 2; ++k3)
				{
					for(int j4 = -1; j4 <= 2; ++j4)
					{
						if ((k3 < 0 || k3 > 1 || j4 < 0 || j4 > 1) && rand.nextInt(3) <= 0)
						{
							int l4 = rand.nextInt(3) + 2;
							for(int i5 = 0; i5 < l4; ++i5) this.func_208533_a(changedBlocks, worldIn, new BlockPos(j + k3, i2 - i5 - 1, l + j4));
							for(int j5 = -1; j5 <= 1; ++j5) for(int l2 = -1; l2 <= 1; ++l2) this.func_202414_a(worldIn, k1 + k3 + j5, i2, l1 + j4 + l2);
							for(int k5 = -2; k5 <= 2; ++k5) for(int l5 = -2; l5 <= 2; ++l5) if (Math.abs(k5) != 2 || Math.abs(l5) != 2) this.func_202414_a(worldIn, k1 + k3 + k5, i2 - 1, l1 + j4 + l5);
						}
					}
				}
				return true;
			}
		}
		else return false;
	}

	private boolean placeTreeOfHeight(IBlockReader worldIn, BlockPos pos, int height)
	{
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for(int l = 0; l <= height + 1; ++l)
		{
			int i1 = 1;
			if (l == 0) i1 = 0;
			if (l >= height - 1) i1 = 2;
			for(int j1 = -i1; j1 <= i1; ++j1) for(int k1 = -i1; k1 <= i1; ++k1) if (!this.canGrowInto(worldIn, blockpos$mutableblockpos.setPos(i + j1, j + l, k + k1))) return false;
		}
		return true;
	}

	private void func_208533_a(Set<BlockPos> p_208533_1_, IWorld p_208533_2_, BlockPos p_208533_3_)
	{
		if (this.canGrowInto(p_208533_2_, p_208533_3_)) this.func_208520_a(p_208533_1_, p_208533_2_, p_208533_3_, SCARLET_LOG);
	}

	private void func_202414_a(IWorld p_202414_1_, int p_202414_2_, int p_202414_3_, int p_202414_4_)
	{
		BlockPos blockpos = new BlockPos(p_202414_2_, p_202414_3_, p_202414_4_);
		if (p_202414_1_.getBlockState(blockpos).isAir(p_202414_1_, blockpos)) this.setBlockState(p_202414_1_, blockpos, SCARLET_LEAVES);
	}
}