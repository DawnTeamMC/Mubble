package hugman.mod.objects.costume;

import hugman.mod.init.technical.MubbleTags;
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
    	player.inventory.clearMatchingItems(item -> MubbleTags.Items.COINS.contains(item.getItem()), 1);
    }
}