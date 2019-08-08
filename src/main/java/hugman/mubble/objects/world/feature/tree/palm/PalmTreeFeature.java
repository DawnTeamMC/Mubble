package hugman.mubble.objects.world.feature.tree.palm;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraftforge.common.IPlantable;

public class PalmTreeFeature extends AbstractTreeFeature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_LEAVES = new ResourceLocation(Mubble.MOD_ID, "palm_tree/leaves");
	private static final BlockState LOG = MubbleBlocks.PALM_LOG.getDefaultState();
	//private static final IBlockState LEAVES = MubbleBlocks.PALM_LEAVES.getDefaultState();
	private static final IPlantable SAPLING = (IPlantable)MubbleBlocks.PALM_SAPLING;
	
	public PalmTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean notify)
	{
		super(configFactory, notify);
	}

	@Override
	public boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox p_208519_5_)
	{
	    //TemplateManager templatemanager = worldIn.getSaveHandler().getStructureTemplateManager();
	    //Template template = templatemanager.getTemplateDefaulted(STRUCTURE_LEAVES);
	    ChunkPos chunkpos = new ChunkPos(position);
	    MutableBoundingBox mutableboundingbox = new MutableBoundingBox(chunkpos.getXStart() - 3, 0, chunkpos.getZStart() - 3, chunkpos.getXEnd() + 3, 256, chunkpos.getZEnd() + 3);
	    PlacementSettings placementsettings = (new PlacementSettings()).setRotation(Rotation.NONE).setBoundingBox(mutableboundingbox).setRandom(rand);
		int i = rand.nextInt(4) + 10;
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

	         if (!flag) return false;
	         else if (isSoil(worldIn, position.down(), SAPLING) && position.getY() < worldIn.getMaxHeight() - i - 1)
			 {
			 	this.setDirtAt(worldIn, position.down(), position);

			 	for(int l2 = position.getY() - 3 + i; l2 <= position.getY() + i; ++l2)
			 	{
			 		int l3 = l2 - (position.getY() + i);
			 		int j4 = 1 - l3 / 2;
			 		//template.addBlocksToWorld(worldIn, new BlockPos(position.getX() - 3, position.getY() + i, position.getZ() - 3), placementsettings, 19);
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
		return false;
	}
	
	/*private void setLeaves(IWorld worldIn, int p_202414_2_, int p_202414_3_, int p_202414_4_)
	{
		BlockPos blockpos = new BlockPos(p_202414_2_, p_202414_3_, p_202414_4_);
		if (worldIn.getBlockState(blockpos).isAir(worldIn, blockpos)) this.setBlockState(worldIn, blockpos, LEAVES);
	}*/
}