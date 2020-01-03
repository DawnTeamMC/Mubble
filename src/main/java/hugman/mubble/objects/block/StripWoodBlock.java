package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StripWoodBlock extends PillarBlock
{
    public StripWoodBlock(Settings builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
    {
    	Block block = Blocks.OAK_LOG;
    	if(this == MubbleBlocks.PALM_LOG) block = MubbleBlocks.STRIPPED_PALM_LOG;
    	if(this == MubbleBlocks.PALM_WOOD) block = MubbleBlocks.STRIPPED_PALM_WOOD;
    	if(this == MubbleBlocks.SCARLET_LOG) block = MubbleBlocks.STRIPPED_SCARLET_LOG;
    	if(this == MubbleBlocks.SCARLET_WOOD) block = MubbleBlocks.STRIPPED_SCARLET_WOOD;
    	if(this == MubbleBlocks.CHERRY_OAK_LOG) block = MubbleBlocks.STRIPPED_CHERRY_OAK_LOG;
    	if(this == MubbleBlocks.CHERRY_OAK_WOOD) block = MubbleBlocks.STRIPPED_CHERRY_OAK_WOOD;
    	if(this == MubbleBlocks.PRESS_GARDEN_LOG) block = MubbleBlocks.STRIPPED_PRESS_GARDEN_LOG;
    	if(this == MubbleBlocks.PRESS_GARDEN_WOOD) block = MubbleBlocks.STRIPPED_PRESS_GARDEN_WOOD;
    	ItemStack item = player.getStackInHand(handIn);
    	if(item.getItem() instanceof AxeItem)
    	{
    		worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
    		if(!worldIn.isClient)
    		{
    			worldIn.setBlockState(pos, block.getDefaultState().with(PillarBlock.AXIS, state.get(PillarBlock.AXIS)), 11);
                if (player != null)
                {
                	item.damage(1, player, (p_214023_1_) ->
                	{
                		p_214023_1_.sendToolBreakStatus(handIn);
                	});
                }
    		}
            return ActionResult.SUCCESS;
    	}
    	return ActionResult.PASS;
    }
}
