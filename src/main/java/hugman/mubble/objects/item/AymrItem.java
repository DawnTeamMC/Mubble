package hugman.mubble.objects.item;

import com.google.common.collect.Multimap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class AymrItem extends Item
{
    public AymrItem(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
    	Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
    	if(slot == EquipmentSlotType.MAINHAND)
    	{
    		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 5.0D, AttributeModifier.Operation.ADDITION));
    		multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -3.5D, AttributeModifier.Operation.ADDITION));
    	}
    	return multimap;
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity victim, LivingEntity sender)
    {
    	stack.damageItem(2, sender, (entity) ->
    	{
    		entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player)
    {
    	if(player.getCooledAttackStrength(0.0F) == 1.0F)
    	{
    		player.getEntityWorld().removeBlock(pos, true);
    	}
    	return super.onBlockStartBreak(itemstack, pos, player);
    }
}
