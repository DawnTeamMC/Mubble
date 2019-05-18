package hugman.mod.util;

import hugman.mod.init.MubblePotionEffects;
import hugman.mod.init.MubbleTags;
import hugman.mod.init.world.MubbleDimensions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
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
			entity.motionY -= (double)((float)(entity.getActivePotionEffect(MubblePotionEffects.HEAVINESS).getAmplifier() + 1) * 0.05F);
		}
	}
	
	@SubscribeEvent
	public static void tick(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		ItemStack itemHead = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		ItemStack itemMainHand = entity.getHeldItemMainhand();
		ItemStack itemOffHand = entity.getHeldItemOffhand();
		if(world.getDimension().getType() == MubbleDimensions.PERMAFROST_TYPE)
		{
			entity.addPotionEffect(new PotionEffect(MubblePotionEffects.HEAVINESS, 5, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 5, 0));
			entity.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 5, 0));
			/*if(itemMainHand != new ItemStack(Blocks.TORCH) && itemOffHand != new ItemStack(Blocks.TORCH))
			{

			}*/
		}
		if(!world.isRemote)
		{
			if(MubbleTags.Items.WEIGHT_HEAVY.contains(itemHead.getItem())) entity.addPotionEffect(new PotionEffect(MubblePotionEffects.HEAVINESS, 5, 0));
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