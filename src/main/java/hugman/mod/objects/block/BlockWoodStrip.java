package hugman.mod.objects.block;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWoodStrip extends BlockRotatedPillar
{
    public BlockWoodStrip(Properties builder)
    {
        super(builder);
    }
    
    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	Block block = Blocks.OAK_LOG;
    	if(this == MubbleBlocks.PALM_LOG) block = MubbleBlocks.STRIPPED_PALM_LOG;
    	if(this == MubbleBlocks.PALM_WOOD) block = MubbleBlocks.STRIPPED_PALM_WOOD;
    	if(this == MubbleBlocks.SCARLET_LOG) block = MubbleBlocks.STRIPPED_SCARLET_LOG;
    	if(this == MubbleBlocks.SCARLET_WOOD) block = MubbleBlocks.STRIPPED_SCARLET_WOOD;
    	if(this == MubbleBlocks.CHERRY_OAK_LOG) block = MubbleBlocks.STRIPPED_CHERRY_OAK_LOG;
    	if(this == MubbleBlocks.CHERRY_OAK_WOOD) block = MubbleBlocks.STRIPPED_CHERRY_OAK_WOOD;
    	ItemStack item = player.getHeldItem(hand);
    	if(item.getItem() instanceof ItemAxe)
    	{
    		worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
    		if(!worldIn.isRemote)
    		{
    			worldIn.setBlockState(pos, block.getDefaultState().with(BlockRotatedPillar.AXIS, state.get(BlockRotatedPillar.AXIS)), 11);
                if (player != null)
                {
                	item.damageItem(1, player);
                }
    		}
            return true;
    	}
    	return super.onBlockActivated(state, worldIn, pos, player, hand, side, hitX, hitY, hitZ);
    }
}
