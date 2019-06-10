package hugman.mod.objects.costume;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class CostumeGuardianMask extends CostumeHead
{    
    public CostumeGuardianMask(Item.Properties builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	if(player.isSneaking() && player.getCooldownTracker().getCooldown(this, 0) == 0)
    	{
    		player.getCooldownTracker().setCooldown(this, 25);
    		player.addStat(StatList.ITEM_USED.get(this));
    	}
    }
}