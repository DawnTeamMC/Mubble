package hugman.mubble.objects.costume;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class GuardianMaskCostume extends HeadCostume
{    
    public GuardianMaskCostume(Item.Properties builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
    	if(player.isSneaking() && player.getCooldownTracker().getCooldown(this, 0) == 0)
    	{
    		player.getCooldownTracker().setCooldown(this, 25);
    		player.addStat(Stats.ITEM_USED.get(this));
    	}
    }
}