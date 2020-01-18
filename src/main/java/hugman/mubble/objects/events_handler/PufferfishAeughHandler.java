/*package hugman.mubble.objects.events_handler;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class PufferfishAeughHandler
{
	@SubscribeEvent
	public static void onRightClick(EntityInteract event)
	{
		PlayerEntity player = event.getPlayer();
		Entity entity = event.getTarget();
		Hand hand = event.getHand();
		ItemStack itemStack = player.getHeldItem(hand);
		
		if(itemStack.getItem() == Items.CARROT && entity.getType() == EntityType.PUFFERFISH)
		{
			PufferfishEntity pufferfish = (PufferfishEntity)entity;
			if(pufferfish.getPuffState() >= 1 && pufferfish.isAlive())
			{
				if(!player.abilities.isCreativeMode)
				{
					itemStack.shrink(1);
				}
				player.swingArm(hand);
				pufferfish.playSound(MubbleSounds.ENTITY_PUFFERFISH_AEUGH, 0.6F, 1.0F);
				event.setCancellationResult(ActionResultType.SUCCESS);
			}
		}
	}
}*/
