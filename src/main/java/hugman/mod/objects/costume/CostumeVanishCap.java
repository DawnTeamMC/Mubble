package hugman.mod.objects.costume;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CostumeVanishCap extends CostumeHead
{    
    public CostumeVanishCap(Item.Properties builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	if(!world.isRemote && player.isSneaking())
    	{
    		player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 2, 0));
    	}
    }
}