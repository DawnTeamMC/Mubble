package hugman.mubble.objects.item;

import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AymrItem extends Item
{
    public AymrItem(Item.Settings builder)
    {
        super(builder);
    }
    
    @Override
    public Multimap<String, EntityAttributeModifier> getModifiers(EquipmentSlot slot)
    {
    	Multimap<String, EntityAttributeModifier> multimap = super.getModifiers(slot);
    	if(slot == EquipmentSlot.MAINHAND)
    	{
    		multimap.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_UUID, "Tool modifier", 5.0D, EntityAttributeModifier.Operation.ADDITION));
    		multimap.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Tool modifier", -3.5D, EntityAttributeModifier.Operation.ADDITION));
    	}
    	return multimap;
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity victim, LivingEntity sender)
    {
    	stack.damage(2, sender, (entity) ->
    	{
    		entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }
    
    @Override
    public boolean postMine(ItemStack itemstack, World world, BlockState state, BlockPos pos, LivingEntity player)
    {
    	if(((PlayerEntity) player).getAttackCooldownProgress(0.0F) == 1.0F)
    	{
    		player.getEntityWorld().removeBlock(pos, true);
    	}
    	return super.postMine(itemstack, world, state, pos, player);
    }
}
