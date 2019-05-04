package hugman.mod.util;

import hugman.mod.init.MubblePotionEffects;
import hugman.mod.init.MubbleTags;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
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
		EntityLivingBase entity = event.getEntityLiving();
		if(entity.isPotionActive(MubblePotionEffects.HEAVINESS))
		{
			entity.motionY += (double)((float)(entity.getActivePotionEffect(MubblePotionEffects.HEAVINESS).getAmplifier() + 1) * -0.05F);
		}
	}
	
	@SubscribeEvent
	public static void tick(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		ItemStack armor = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		if(!world.isRemote)
		{
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(armor.getItem())) entity.addPotionEffect(new PotionEffect(MubblePotionEffects.HEAVINESS, 10, 0));
		}
	}
	
	@SubscribeEvent
	public static void secretMessages(ClientChatEvent event)
	{
		if(event.getMessage() == "shrug")
		{
			event.setMessage("¯\\_(ツ)_/¯");
		}
	}
}