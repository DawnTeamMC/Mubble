package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class StripWoodBlock extends RotatedPillarBlock
{
    public StripWoodBlock(Properties builder)
    {
        super(builder);
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
    	Block block = Blocks.OAK_LOG;
    	if(this == MubbleBlocks.PALM_LOG) block = MubbleBlocks.STRIPPED_PALM_LOG;
    	if(this == MubbleBlocks.PALM_WOOD) block = MubbleBlocks.STRIPPED_PALM_WOOD;
    	if(this == MubbleBlocks.SCARLET_LOG) block = MubbleBlocks.STRIPPED_SCARLET_LOG;
    	if(this == MubbleBlocks.SCARLET_WOOD) block = MubbleBlocks.STRIPPED_SCARLET_WOOD;
    	if(this == MubbleBlocks.CHERRY_OAK_LOG) block = MubbleBlocks.STRIPPED_CHERRY_OAK_LOG;
    	if(this == MubbleBlocks.CHERRY_OAK_WOOD) block = MubbleBlocks.STRIPPED_CHERRY_OAK_WOOD;
    	ItemStack item = player.getHeldItem(handIn);
    	if(item.getItem() instanceof AxeItem)
    	{
    		worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
    		if(!worldIn.isRemote)
    		{
    			worldIn.setBlockState(pos, block.getDefaultState().with(RotatedPillarBlock.AXIS, state.get(RotatedPillarBlock.AXIS)), 11);
                if (player != null)
                {
                	item.damageItem(1, player, (p_214023_1_) ->
                	{
                		p_214023_1_.sendBreakAnimation(handIn);
                	});
                }
    		}
            return true;
    	}
    	return false;
    }
}
