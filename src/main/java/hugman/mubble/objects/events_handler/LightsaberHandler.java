/*package hugman.mubble.objects.events_handler;

import hugman.mubble.objects.item.LightsaberItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class LightsaberHandler
{
	@SubscribeEvent
	public static void onLightsaberPull(LivingEquipmentChangeEvent event)
	{
		LivingEntity entityIn = event.getEntityLiving();
		World worldIn = entityIn.getEntityWorld();
		ItemStack to = event.getTo();
		ItemStack from = event.getFrom();
		if(!(to.getDisplayName() == from.getDisplayName()))
		{
			if(to.getItem() instanceof LightsaberItem)
			{
				((LightsaberItem)to.getItem()).onPullOut(entityIn, worldIn);
			}
			if(from.getItem() instanceof LightsaberItem)
			{
				((LightsaberItem)from.getItem()).onPullIn(entityIn, worldIn);
			}
		}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void clientTick(ClientTickEvent event)
	{
		if(event.phase == Phase.START)
		{
			if(LightsaberItem.idleTimer <= 95)
			{
				LightsaberItem.idleTimer++;
			}
			else
			{
				LightsaberItem.idleTimer = 0;
			}
		}
	}
}*/