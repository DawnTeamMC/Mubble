package hugman.mubble.objects.events_handlers;

import java.util.Random;

import hugman.mubble.init.MubbleCostumes;
import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.objects.CalendarEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class SeasonalAdditionHandler
{
	@SubscribeEvent
	public static void onSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		Entity fEntity = event.getEntity();
		Random rand = new Random();
		if(fEntity instanceof MobEntity)
		{
			MobEntity entity = (MobEntity)fEntity;
			if(MubbleTags.EntityTypes.CAN_WEAR_HELMET.contains(entity.getType()))
			{
				if(entity.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty() && CalendarEvents.isChristmasSeason)
				{
					if(rand.nextFloat() < (float)CalendarEvents.getDayToday() / 25.0f)
					{
						entity.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(MubbleCostumes.CHRISTMAS_HAT));
						entity.setDropChance(EquipmentSlotType.HEAD, 0.0F);
					}
				}
			}
		}
	}
}