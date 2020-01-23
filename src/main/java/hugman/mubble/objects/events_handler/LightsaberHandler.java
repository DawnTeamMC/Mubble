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
}*/
