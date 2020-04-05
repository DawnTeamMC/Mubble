package hugman.mubble.objects.item;

import com.google.common.collect.Multimap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public ActionResultType onItemUse(ItemUseContext context)
    {
    	BlockPos pos = context.getPos();
    	PlayerEntity player = context.getPlayer();
    	World world = context.getWorld();
    	
    	if(player.getCooledAttackStrength(0.0F) == 1.0F)
    	{
    		if(world.isRemote)
    		{
    			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F, false);
			}
    		world.addParticle(ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(), 1.0D, 0.0D, 0.0D);
    		world.addParticle(ParticleTypes.EXPLOSION_EMITTER, pos.getX(), pos.getY(), pos.getZ(), 1.0D, 0.0D, 0.0D);
    		world.removeBlock(pos, true);
    		return ActionResultType.SUCCESS;
    	}
		return ActionResultType.FAIL;
    }
}
