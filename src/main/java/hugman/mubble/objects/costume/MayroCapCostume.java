package hugman.mubble.objects.costume;

import hugman.mubble.init.data.MubbleTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class MayroCapCostume extends HeadCostume
{    
    public MayroCapCostume(Item.Settings builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
    @Override
    public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks)
    {
    	((PlayerEntity) player).inventory.method_7369(item -> MubbleTags.Items.COINS.contains(item.getItem()), 1);
    	super.usageTick(world, player, stack, remainingUseTicks);
    }
}