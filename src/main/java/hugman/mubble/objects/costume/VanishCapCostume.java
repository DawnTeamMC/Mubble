package hugman.mubble.objects.costume;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class VanishCapCostume extends HeadCostume
{    
    public VanishCapCostume(Item.Properties builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
    	if(!world.isRemote && player.isSneaking())
    	{
    		player.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 2, 0));
    	}
    	super.onArmorTick(stack, world, player);
    }
}