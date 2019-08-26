package hugman.mubble.objects.world.feature.tree.template;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.HugeTreesFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;

public class MegaTreeFeature extends HugeTreesFeature<NoFeatureConfig>
{
	public MegaTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify, int baseHeightIn, int extraRandomHeightIn, Block log, Block leaves, Block sapling)
	{
		super(configFactory, notify, baseHeightIn, extraRandomHeightIn, log.getDefaultState(), leaves.getDefaultState());
		setSapling((IPlantable)sapling);
	}
	
	@Override
	public boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox boundingBox)
	{
		int i = this.getHeight(rand);
		if (!this.func_203427_a(worldIn, position, i))
		{
			return false;
		}
		else
		{
			this.func_214601_d(worldIn, position.up(i), 2, boundingBox, changedBlocks);
			for(int j = position.getY() + i - 2 - rand.nextInt(4); j > position.getY() + i / 2; j -= 2 + rand.nextInt(4))
			{
				float f = rand.nextFloat() * ((float)Math.PI * 2F);
	            int k = position.getX() + (int)(0.5F + MathHelper.cos(f) * 4.0F);
	            int l = position.getZ() + (int)(0.5F + MathHelper.sin(f) * 4.0F);

	            for(int i1 = 0; i1 < 5; ++i1)
	            {
	            	k = position.getX() + (int)(1.5F + MathHelper.cos(f) * (float)i1);
	            	l = position.getZ() + (int)(1.5F + MathHelper.sin(f) * (float)i1);
	            	this.setLogState(changedBlocks, worldIn, new BlockPos(k, j - 3 + i1 / 2, l), this.trunk, boundingBox);
	            }

	            int j2 = 1 + rand.nextInt(2);
	            int j1 = j;

	            for(int k1 = j - j2; k1 <= j1; ++k1)
	            {
	            	int l1 = k1 - j1;
	            	this.func_222838_b(worldIn, new BlockPos(k, k1, l), 1 - l1, boundingBox, changedBlocks);
	            }
			}
			
	         for(int i2 = 0; i2 < i; ++i2)
	         {
	             BlockPos blockpos = position.up(i2);
	             if (func_214587_a(worldIn, blockpos))
	             {
	                this.setLogState(changedBlocks, worldIn, blockpos, this.trunk, boundingBox);
	             }

	             if (i2 < i - 1)
	             {
	                BlockPos blockpos1 = blockpos.east();
	                if (func_214587_a(worldIn, blockpos1))
	                {
	                   this.setLogState(changedBlocks, worldIn, blockpos1, this.trunk, boundingBox);
	                }

	                BlockPos blockpos2 = blockpos.south().east();
	                if (func_214587_a(worldIn, blockpos2))
	                {
	                   this.setLogState(changedBlocks, worldIn, blockpos2, this.trunk, boundingBox);
	                }

	                BlockPos blockpos3 = blockpos.south();
	                if(func_214587_a(worldIn, blockpos3))
	                {
	                   this.setLogState(changedBlocks, worldIn, blockpos3, this.trunk, boundingBox);
	                }
	             }
	         }
	         return true;
		}
	}

	private void func_214601_d(IWorldGenerationReader worldIn, BlockPos pos, int p_214601_3_, MutableBoundingBox p_214601_4_, Set<BlockPos> changedBlocks)
	{
		for(int j = -2; j <= 0; ++j)
		{
			this.func_222839_a(worldIn, pos.up(j), p_214601_3_ + 1 - j, p_214601_4_, changedBlocks);
		}
	}
}