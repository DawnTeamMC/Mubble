package hugman.mod.objects.costume;

import java.util.function.Predicate;

import hugman.mod.init.MubbleItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CostumeMayroCap extends CostumeSimple
{    
    public CostumeMayroCap()
    {
        super("mayro_cap", SoundEvents.BLOCK_SLIME_BLOCK_BREAK, EntityEquipmentSlot.HEAD);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	player.inventory.clearMatchingItems((Predicate<ItemStack>) MubbleItems.YELLOW_COIN, 1);
    }
}