package hugman.mubble.objects.item;

import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AymrItem extends Item
{
    public AymrItem(Item.Settings builder)
    {
        super(builder);
    }
    
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot)
    {
    	Multimap<EntityAttribute, EntityAttributeModifier> multimap = super.getAttributeModifiers(slot);
    	if(slot == EquipmentSlot.MAINHAND)
    	{
    		multimap.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 5.0D, EntityAttributeModifier.Operation.ADDITION));
    		multimap.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -3.5D, EntityAttributeModifier.Operation.ADDITION));
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
    public ActionResult useOnBlock(ItemUsageContext context)
    {
    	BlockPos pos = context.getBlockPos();
    	PlayerEntity player = context.getPlayer();
    	World world = context.getWorld();
    	
    	if(((PlayerEntity) player).getAttackCooldownProgress(0.0F) == 1.0F)
    	{
    		if(world.isClient)
    		{
    			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
			}
    		world.addParticle(ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(), 1.0D, 0.0D, 0.0D);
    		world.addParticle(ParticleTypes.EXPLOSION_EMITTER, pos.getX(), pos.getY(), pos.getZ(), 1.0D, 0.0D, 0.0D);
    		world.removeBlock(pos, true);
    		return ActionResult.SUCCESS;
    	}
    	return ActionResult.FAIL;
    }
}
