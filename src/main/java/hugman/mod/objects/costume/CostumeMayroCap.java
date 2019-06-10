package hugman.mod.objects.costume;

import hugman.mod.init.MubbleTags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CostumeMayroCap extends CostumeHead
{    
    public CostumeMayroCap(Item.Properties builder)
    {
        super(builder , SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	player.inventory.clearMatchingItems(item -> MubbleTags.Items.COINS.contains(item.getItem()), 1);
    }
}