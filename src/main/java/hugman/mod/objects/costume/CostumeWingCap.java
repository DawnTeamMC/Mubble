package hugman.mod.objects.costume;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CostumeWingCap extends CostumeHead
{    
    public CostumeWingCap(Item.Properties builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		return repair.getItem() == Items.FEATHER;
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	if(isUsable(stack) && player.isSprinting()) stack.damageItem(1, player);
    	if(!world.isRemote && isUsable(stack) && player.isSprinting())
    	{
    		player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 1, 2));
    		player.fallDistance = 0f;
    	}
    }
}