package hugman.mod.util;

import hugman.mod.init.MubbleEffects;
import hugman.mod.init.MubbleTags;
import hugman.mod.init.world.MubbleDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class EventExtents
{
	@SubscribeEvent
	public static void jump(LivingJumpEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		if(entity.isPotionActive(MubbleEffects.HEAVINESS))
		{
			Vec3d vec3d = entity.getMotion();
			entity.setMotion(vec3d.x, vec3d.y - (float)(entity.getActivePotionEffect(MubbleEffects.HEAVINESS).getAmplifier() + 1) * 0.05F, vec3d.z);
		}
	}
	
	@SubscribeEvent
	public static void tick(LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		ItemStack itemHead = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);
		ItemStack itemMainHand = entity.getHeldItemMainhand();
		ItemStack itemOffHand = entity.getHeldItemOffhand();
		if(world.getDimension().getType() == MubbleDimensions.PERMAFROST_TYPE)
		{
			entity.addPotionEffect(new EffectInstance(MubbleEffects.HEAVINESS, 5, 0));
			entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 0));
			entity.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 5, 0));
			/*if(itemMainHand != new ItemStack(Blocks.TORCH) && itemOffHand != new ItemStack(Blocks.TORCH))
			{

			}*/
		}
		if(!world.isRemote)
		{
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(itemHead.getItem())) entity.addPotionEffect(new EffectInstance(MubbleEffects.HEAVINESS, 5, 0));
		}
	}
	
	@SubscribeEvent
	public static void secretMessages(ClientChatEvent event)
	{
		if(event.getOriginalMessage() == "shrug")
		{
			event.setMessage("¯\\_(ツ)_/¯");
		}
	}
}