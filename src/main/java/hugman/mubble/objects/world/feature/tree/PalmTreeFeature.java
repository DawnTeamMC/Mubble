package hugman.mubble.objects.world.feature.tree;

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

public class PalmTreeFeature extends AbstractTreeFeature<BranchedTreeFeatureConfig>
{
	public PalmTreeFeature(Function<Dynamic<?>, ? extends BranchedTreeFeatureConfig> configFactory)
	{
		super(configFactory);
	}

	@Override
	public boolean generate(ModifiableTestableWorld worldIn, Random rand, BlockPos position, Set<BlockPos> changedBlocks, Set<BlockPos> leaves, BlockBox boundingBox, BranchedTreeFeatureConfig config)
	{
		int i = rand.nextInt(4) + 17;
		boolean flag = true;
		if(position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE, position).getY())
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
			
			if(!flag)
	        {
				return false;
	        }
			else if(isDirtOrGrass(worldIn, position.down()) && position.getY() < worldIn.getTopPosition(Heightmap.Type.WORLD_SURFACE, position).getY() - i - 1)
			{
				int originX = position.getX();
		 		int originY = position.getY() + i - 1;
		 		int originZ = position.getZ();
		 		this.setToDirt(worldIn, position);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY + 1, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 1, originY + 1, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 1, originY + 1, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY + 1, originZ + 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY + 1, originZ - 1, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 1, originY, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 1, originY, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY, originZ + 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY, originZ - 1, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 1, originY, originZ + 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 1, originY, originZ - 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 1, originY, originZ + 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 1, originY, originZ - 1, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY, originZ + 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY, originZ - 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY, originZ + 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY, originZ - 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 1, originY, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 1, originY, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY, originZ - 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 1, originY, originZ - 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 1, originY, originZ - 2, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 1, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 1, originZ + 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 1, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 1, originZ - 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 1, originZ - 2, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 1, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 1, originZ + 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 1, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 1, originZ - 1, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 1, originZ - 2, boundingBox, config);

		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY - 1, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 1, originY - 1, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 1, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 1, originY - 1, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 1, originZ + 2, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY - 1, originZ - 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 1, originY - 1, originZ - 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 1, originZ - 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 1, originY - 1, originZ - 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 1, originZ - 2, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 2, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 2, originY - 2, originZ - 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 2, originZ + 2, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 2, originY - 2, originZ - 2, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 3, originY - 1, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 3, originY - 1, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY - 1, originZ + 3, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY - 1, originZ - 3, boundingBox, config);

		 		placeLeaves(changedBlocks, worldIn, rand, originX + 3, originY - 2, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 3, originY - 2, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY - 2, originZ + 3, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY - 2, originZ - 3, boundingBox, config);
		 		
		 		placeLeaves(changedBlocks, worldIn, rand, originX + 3, originY - 3, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX - 3, originY - 3, originZ, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY - 3, originZ + 3, boundingBox, config);
		 		placeLeaves(changedBlocks, worldIn, rand, originX, originY - 3, originZ - 3, boundingBox, config);
			 	for(int i3 = 0; i3 < i; ++i3)
			 	{
			 		if(isAirOrLeaves(worldIn, position.up(i3)) || isReplaceablePlant(worldIn, position.up(i3)))
			 		{
			 			this.setLogBlockState(worldIn, rand, position.up(i3), changedBlocks, boundingBox, config);
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
	
	public void placeLeaves(Set<BlockPos> changedBlocks, ModifiableTestableWorld worldIn, Random random, int x, int y, int z, BlockBox boundingBox, BranchedTreeFeatureConfig config)
	{
		BlockPos blockPos = new BlockPos(x, y, z);
		if (isAirOrLeaves(worldIn, blockPos) || isReplaceablePlant(worldIn, blockPos))
		{
			this.setLogBlockState(worldIn, random, blockPos, changedBlocks, boundingBox, config);
		}
	}
}