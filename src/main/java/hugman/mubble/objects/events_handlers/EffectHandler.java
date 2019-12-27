package hugman.mubble.objects.events_handlers;

import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class EffectHandler
{
	@SubscribeEvent
	public static void onJump(LivingJumpEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		if(entity.isPotionActive(MubbleEffects.HEAVINESS))
		{
			Vec3d vec3d = entity.getMotion();
			entity.setMotion(vec3d.x, vec3d.y - (float)(entity.getActivePotionEffect(MubbleEffects.HEAVINESS).getAmplifier() + 1) * 0.05F, vec3d.z);
		}
	}
	
	@SubscribeEvent
	public static void onTick(LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		ItemStack headItem = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);
		if(!world.isRemote)
		{
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(headItem.getItem()))
			{
				entity.addPotionEffect(new EffectInstance(MubbleEffects.HEAVINESS, 25, 0));
			}
		}
	}
}
