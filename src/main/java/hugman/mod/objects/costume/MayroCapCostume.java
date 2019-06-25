package hugman.mod.objects.costume;

import hugman.mod.init.MubbleTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class MayroCapCostume extends HeadCostume
{    
    public MayroCapCostume(Item.Properties builder)
    {
        super(builder , SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
    	player.inventory.clearMatchingItems(item -> MubbleTags.Items.COINS.contains(item.getItem()), 1);
    }
}