package hugman.mod.objects.costume;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CostumeMayroCap extends CostumeSimple
{    
    public CostumeMayroCap()
    {
        super("mayro_cap", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, EntityEquipmentSlot.HEAD);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	//player.inventory.clearMatchingItems((Predicate<ItemStack>) MubbleItems.YELLOW_COIN, 1);
    }
}