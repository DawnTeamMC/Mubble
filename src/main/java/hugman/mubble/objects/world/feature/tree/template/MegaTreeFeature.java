package hugman.mubble.objects.world.feature.tree.template;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.MegaTreeFeatureConfig;

public class MegaTreeFeature extends net.minecraft.world.gen.feature.MegaTreeFeature<MegaTreeFeatureConfig>
{
	public MegaTreeFeature(Function<Dynamic<?>, ? extends MegaTreeFeatureConfig> configFactory, boolean notify, int baseHeightIn, int extraRandomHeightIn, Block log, Block leaves, Block sapling)
	{
		super(configFactory);
	}
	
	@Override
	public boolean generate(ModifiableTestableWorld worldIn, Random rand, BlockPos position, Set<BlockPos> changedBlocks, Set<BlockPos> leavesPositions, BlockBox box, MegaTreeFeatureConfig config)
	{
		int i = this.getHeight(rand, config);
		if (!this.checkTreeFitsAndReplaceGround(worldIn, position, i))
		{
			return false;
		}
		else
		{
			this.func_214601_d(worldIn, rand, position.up(i), box, changedBlocks, config);
			for(int j = position.getY() + i - 2 - rand.nextInt(4); j > position.getY() + i / 2; j -= 2 + rand.nextInt(4))
			{
				float f = rand.nextFloat() * ((float)Math.PI * 2F);
	            int k = position.getX() + (int)(0.5F + MathHelper.cos(f) * 4.0F);
	            int l = position.getZ() + (int)(0.5F + MathHelper.sin(f) * 4.0F);

	            for(int i1 = 0; i1 < 5; ++i1)
	            {
	            	k = position.getX() + (int)(1.5F + MathHelper.cos(f) * (float)i1);
	            	l = position.getZ() + (int)(1.5F + MathHelper.sin(f) * (float)i1);
	            	this.setLogBlockState(worldIn, rand, new BlockPos(k, j - 3 + i1 / 2, l), changedBlocks, box, config);
	            }

	            int j2 = 1 + rand.nextInt(2);
	            int j1 = j;

	            for(int k1 = j - j2; k1 <= j1; ++k1)
	            {
	            	int l1 = k1 - j1;
	            	this.makeSquaredLeafLayer(worldIn, rand, new BlockPos(k, k1, l), 1 - l1, changedBlocks, box, config);
	            }
			}
			
	         for(int i2 = 0; i2 < i; ++i2)
	         {
	             BlockPos blockpos = position.up(i2);
	             if (canTreeReplace(worldIn, blockpos))
	             {
	                this.setLogBlockState(worldIn, rand, blockpos, changedBlocks, box, config);
	             }

	             if (i2 < i - 1)
	             {
	                BlockPos blockpos1 = blockpos.east();
	                if (canTreeReplace(worldIn, blockpos1))
	                {
	                   this.setLogBlockState(worldIn, rand, blockpos1, changedBlocks, box, config);
	                }

	                BlockPos blockpos2 = blockpos.south().east();
	                if (canTreeReplace(worldIn, blockpos2))
	                {
	                   this.setLogBlockState(worldIn, rand, blockpos2, changedBlocks, box, config);
	                }

	                BlockPos blockpos3 = blockpos.south();
	                if(canTreeReplace(worldIn, blockpos3))
	                {
	                   this.setLogBlockState(worldIn, rand, blockpos3, changedBlocks, box, config);
	                }
	             }
	         }
	         return true;
		}
	}

	private void func_214601_d(ModifiableTestableWorld worldIn, Random random, BlockPos pos, BlockBox p_214601_4_, Set<BlockPos> changedBlocks, MegaTreeFeatureConfig config)
	{
		for(int j = -2; j <= 0; ++j)
		{
			this.setLeavesBlockState(worldIn, random, pos.up(j), changedBlocks, p_214601_4_, config);
		}
	}
}