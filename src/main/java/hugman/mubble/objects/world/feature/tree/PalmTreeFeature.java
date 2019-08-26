package hugman.mubble.objects.world.feature.tree;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;

public class PalmTreeFeature extends AbstractTreeFeature<NoFeatureConfig>
{
	private static final BlockState LOG = MubbleBlocks.PALM_LOG.getDefaultState();
	private static final BlockState LEAVES = MubbleBlocks.PALM_LEAVES.getDefaultState();
	private static final IPlantable SAPLING = (IPlantable)MubbleBlocks.PALM_SAPLING;
	
	public PalmTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify);
	}

	@Override
	public boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox boundingBox)
	{
		int i = rand.nextInt(4) + 17;
		boolean flag = true;
		if(position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getMaxHeight())
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

	         if(!flag) return false;
	         else if(isSoil(worldIn, position.down(), SAPLING) && position.getY() < worldIn.getMaxHeight() - i - 1)
			 {
		 		int originX = position.getX();
		 		int originY = position.getY() + i - 1;
		 		int originZ = position.getZ();
		 		placeLeaves(changedBlocks, worldIn, originX, originY + 1, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 1, originY + 1, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 1, originY + 1, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY + 1, originZ + 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY + 1, originZ - 1, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX + 1, originY, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 1, originY, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY, originZ + 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY, originZ - 1, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX + 1, originY, originZ + 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 1, originY, originZ - 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 1, originY, originZ + 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 1, originY, originZ - 1, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY, originZ + 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY, originZ - 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY, originZ + 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY, originZ - 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 1, originY, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 1, originY, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY, originZ - 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 1, originY, originZ - 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 1, originY, originZ - 2, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 1, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 1, originZ + 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 1, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 1, originZ - 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 1, originZ - 2, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 1, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 1, originZ + 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 1, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 1, originZ - 1, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 1, originZ - 2, boundingBox);

		 		placeLeaves(changedBlocks, worldIn, originX, originY - 1, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 1, originY - 1, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 1, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 1, originY - 1, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 1, originZ + 2, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX, originY - 1, originZ - 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 1, originY - 1, originZ - 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 1, originZ - 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 1, originY - 1, originZ - 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 1, originZ - 2, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 2, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX + 2, originY - 2, originZ - 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 2, originZ + 2, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 2, originY - 2, originZ - 2, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX + 3, originY - 1, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 3, originY - 1, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY - 1, originZ + 3, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY - 1, originZ - 3, boundingBox);

		 		placeLeaves(changedBlocks, worldIn, originX + 3, originY - 2, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 3, originY - 2, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY - 2, originZ + 3, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY - 2, originZ - 3, boundingBox);
		 		
		 		placeLeaves(changedBlocks, worldIn, originX + 3, originY - 3, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX - 3, originY - 3, originZ, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY - 3, originZ + 3, boundingBox);
		 		placeLeaves(changedBlocks, worldIn, originX, originY - 3, originZ - 3, boundingBox);
			 	for(int i3 = 0; i3 < i; ++i3)
			 	{
			 		if(isAirOrLeaves(worldIn, position.up(i3)) || func_214576_j(worldIn, position.up(i3)))
			 		{
			 			this.setLogState(changedBlocks, worldIn, position.up(i3), LOG, boundingBox);
			 		}
			 	}
			 }
		}
		return false;
	}
	
	public void placeLeaves(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, int x, int y, int z, MutableBoundingBox boundingBox)
	{
		BlockPos blockPos = new BlockPos(x, y, z);
		if(isAirOrLeaves(worldIn, blockPos) || func_214576_j(worldIn, blockPos))
		{
			boundingBox.expandTo(new MutableBoundingBox(blockPos, blockPos));
			worldIn.setBlockState(blockPos, LEAVES, 3);
		}
	}
}