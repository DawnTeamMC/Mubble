package hugman.mod.objects.block;

import hugman.mod.init.MubbleTags;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFlowerCloud extends BlockFlower
{
    public BlockFlowerCloud()
    {
        super("cloud_flower");
    }
    
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	if(entityIn instanceof EntityPlayer)
    	{
    		EntityPlayer playerIn = (EntityPlayer)entityIn;
    		ItemStack armor;
    		armor = playerIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
    		if(MubbleTags.Items.CROWNS.contains(armor.getItem()) && !playerIn.isSneaking())
    		{
        		if(!playerIn.isSprinting()) playerIn.motionY = (this.RANDOM.nextInt(31) + 40) / 100D;
    			else playerIn.motionY = 0.7D;
        		playerIn.fallDistance = 0f;
    		}
    	}
    	if(entityIn instanceof EntityItem)
    	{
    		EntityItem itemIn = (EntityItem)entityIn;
        	if(MubbleTags.Items.CROWNS.contains(itemIn.getItem().getItem())) itemIn.motionY = 0.3D;
        	if(MubbleTags.Items.WEIGHT_LIGHT.contains(itemIn.getItem().getItem())) itemIn.motionY = 0.1D;
    	}
    }
}
