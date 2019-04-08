package hugman.mod.objects.world.feature.tree.palm;

import java.util.Random;
import java.util.Set;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.IPlantable;

public class PalmTreeFeature extends AbstractTreeFeature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_LEAVES = new ResourceLocation(Mubble.MOD_ID, "palm_tree/leaves");
	private static final IBlockState LOG = MubbleBlocks.PALM_LOG.getDefaultState();
	//private static final IBlockState LEAVES = MubbleBlocks.PALM_LEAVES.getDefaultState();
	private static final IPlantable SAPLING = (IPlantable)MubbleBlocks.PALM_SAPLING;
	
	public PalmTreeFeature(boolean notify)
	{
		super(notify);
	}

	@Override
	public boolean place(Set<BlockPos> changedBlocks, IWorld worldIn, Random rand, BlockPos position)
	{
	    TemplateManager templatemanager = worldIn.getSaveHandler().getStructureTemplateManager();
	    Template template = templatemanager.getTemplateDefaulted(STRUCTURE_LEAVES);
	    ChunkPos chunkpos = new ChunkPos(position);
	    MutableBoundingBox mutableboundingbox = new MutableBoundingBox(chunkpos.getXStart() - 3, 0, chunkpos.getZStart() - 3, chunkpos.getXEnd() + 3, 256, chunkpos.getZEnd() + 3);
	    PlacementSettings placementsettings = (new PlacementSettings()).setRotation(Rotation.NONE).setBoundingBox(mutableboundingbox).setRandom(rand);
		int i = rand.nextInt(4) + 10;
		boolean flag = true;
		if (position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getWorld().getHeight())
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
	            		if (j >= 0 && j < worldIn.getWorld().getHeight())
	            		{
	            			if (!this.canGrowInto(worldIn, blockpos$mutableblockpos.setPos(l, j, i1))) flag = false;
	            		}
	            		else flag = false;
	            	}
	            }
	         }

	         if (!flag) return false;
	         else
	         {
	        	 boolean isSoil = worldIn.getBlockState(position.down()).canSustainPlant(worldIn, position.down(), net.minecraft.util.EnumFacing.UP, SAPLING);
	             if (isSoil && position.getY() < worldIn.getWorld().getHeight() - i - 1)
	             {
	            	 this.setDirtAt(worldIn, position.down(), position);
            	     template.addBlocksToWorld(worldIn, new BlockPos(position.getX() - 3, position.getY() + i, position.getZ() - 3), placementsettings, 19);
	            	 for(int trunk_height = 0; trunk_height < i; ++trunk_height)
	            	 {
	            		 IBlockState iblockstate1 = worldIn.getBlockState(position.up(trunk_height));
	            		 if (iblockstate1.isAir(worldIn, position.up(trunk_height)) || iblockstate1.isIn(BlockTags.LEAVES)) this.func_208520_a(changedBlocks, worldIn, position.up(trunk_height), LOG);
	            	 }
	            	 return true;
	             }
	             else return false;
	         }
		}
		else return false;
	}
	
	/*private void setLeaves(IWorld worldIn, int p_202414_2_, int p_202414_3_, int p_202414_4_)
	{
		BlockPos blockpos = new BlockPos(p_202414_2_, p_202414_3_, p_202414_4_);
		if (worldIn.getBlockState(blockpos).isAir(worldIn, blockpos)) this.setBlockState(worldIn, blockpos, LEAVES);
	}*/
}