package hugman.mubble.objects.costume;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class WingCapCostume extends HeadCostume
{    
    public WingCapCostume(Item.Properties builder)
    {
        super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }
    
	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		return repair.getItem() == Items.FEATHER;
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
    {
    	if(isUsable(stack) && player.isSprinting())
    	{
        	stack.damageItem(1, player, (p_214023_1_) ->
        	{
        		p_214023_1_.sendBreakAnimation(EquipmentSlotType.HEAD);
        	});
    	}
    	if(!world.isRemote && isUsable(stack) && player.isSprinting())
    	{
    		player.addPotionEffect(new EffectInstance(Effects.LEVITATION, 1, 2));
    		player.fallDistance = 0f;
    	}
    }
}