package hugman.mod.objects.costume;

import hugman.mod.init.technical.MubbleTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CostumeVanishCap extends CostumeSimple
{    
    public CostumeVanishCap()
    {
        super("vanish_cap", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD, new Item.Properties().group(MubbleTabs.MUBBLE_COSTUMES).maxStackSize(1));
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