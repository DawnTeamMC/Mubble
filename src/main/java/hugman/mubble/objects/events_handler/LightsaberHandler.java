/*package hugman.mubble.objects.events_handler;

import hugman.mubble.objects.item.LightsaberItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LightsaberHandler
{
	public static void onLightsaberPull(LivingEquipmentChangeEvent event)
	{
		LivingEntity entityIn = event.getEntityLiving();
		World worldIn = entityIn.getEntityWorld();
		ItemStack to = event.getTo();
		ItemStack from = event.getFrom();
		if(!(to.getName() == from.getName()))
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
	
	public static void onBlockLeftClick(LeftClickBlock event)
	{
		ItemStack itemStack = event.getItemStack();
		if(itemStack.getItem() instanceof LightsaberItem)
		{
			((LightsaberItem)itemStack.getItem()).onSwing(event.getPlayer(), false);
		}
	}
	
	public static void onAirLeftClick(LeftClickEmpty event)
	{
		ItemStack itemStack = event.getItemStack();
		if(itemStack.getItem() instanceof LightsaberItem)
		{
			((LightsaberItem)itemStack.getItem()).onSwing(event.getPlayer(), false);
		}
	}
	
	public static void onAttack(AttackEntityEvent event)
	{
		ItemStack itemStack = event.getPlayer().getActiveItemStack();
		if(itemStack.getItem() instanceof LightsaberItem)
		{
			((LightsaberItem)itemStack.getItem()).onSwing(event.getPlayer(), true);
		}
	}
}*/
