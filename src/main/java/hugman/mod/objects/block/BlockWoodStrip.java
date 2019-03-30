package hugman.mod.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWoodStrip extends BlockRotatedPillar
{
	Block output;
	
    public BlockWoodStrip(String name, Properties properties, Block output, ItemGroup group)
    {
        super(name, properties, group);
        this.output = output;
    }
    
    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	ItemStack item = player.getHeldItem(hand);
    	if(item.getItem() instanceof ItemAxe)
    	{
    		worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            worldIn.setBlockState(pos, this.output.getDefaultState().with(BlockRotatedPillar.AXIS, state.get(BlockRotatedPillar.AXIS)), 11);
            if (player != null)
            {
            	item.damageItem(1, player);
            }
    	}
    	return super.onBlockActivated(state, worldIn, pos, player, hand, side, hitX, hitY, hitZ);
    }
}
