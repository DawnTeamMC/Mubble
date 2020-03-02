package hugman.mubble.objects.event_handler;

import hugman.mubble.objects.item.LightsaberItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
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
		Item to = event.getTo().getItem();
		Item from = event.getFrom().getItem();
		if(!(to instanceof LightsaberItem && from instanceof LightsaberItem))
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
	
	@SubscribeEvent
	public static void onAirLeftClick(LeftClickEmpty event)
	{
		ItemStack itemStack = event.getItemStack();
		if(itemStack.getItem() instanceof LightsaberItem)
		{
			((LightsaberItem)itemStack.getItem()).onSwing(event.getPlayer(), false);
		}
	}
	
	@SubscribeEvent
	public static void onAttack(AttackEntityEvent event)
	{
		Entity target = event.getTarget();
		PlayerEntity player = event.getPlayer();
		ItemStack itemStack = player.getHeldItem(Hand.MAIN_HAND);
		if(itemStack.getItem() instanceof LightsaberItem)
		{
			if(target.canBeAttackedWithItem() && !target.isInvulnerableTo(DamageSource.causePlayerDamage(player)) && target.isAlive() && !target.hitByEntity(player))
			{
				((LightsaberItem)itemStack.getItem()).onSwing(event.getPlayer(), true);
			}
			else
			{
				((LightsaberItem)itemStack.getItem()).onSwing(event.getPlayer(), false);
			}
		}
	}
}