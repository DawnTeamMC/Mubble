package hugman.mubble.objects.event_handler;

import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.util.EnchantmentUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class EnchantmentHandler
{
	@SubscribeEvent
	public static void onEntityDropsItems(LivingDropsEvent event)
	{
		Entity sourceEntity = event.getSource().getTrueSource();
		if(sourceEntity != null)
		{
			if(sourceEntity instanceof PlayerEntity)
			{
				PlayerEntity playerIn = (PlayerEntity)sourceEntity;
				if(EnchantmentUtil.hasEnchantment(MubbleEnchantments.TELEKINESIS, playerIn.getHeldItem(Hand.MAIN_HAND)))
				{
					for(ItemEntity itemEntity : event.getDrops())
					{
						ItemStack itemStack = itemEntity.getItem();
						playerIn.addItemStackToInventory(itemStack);
					}
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityDropsExperience(LivingExperienceDropEvent event)
	{
		if(event.getAttackingPlayer() != null)
		{
			PlayerEntity playerIn = event.getAttackingPlayer();
			if(playerIn.getHeldItem(Hand.MAIN_HAND) != null)
			{
				if(EnchantmentHelper.getEnchantmentLevel(MubbleEnchantments.TELEKINESIS, playerIn.getHeldItem(Hand.MAIN_HAND)) >= 2)
				{
					playerIn.giveExperiencePoints(event.getDroppedExperience());
					event.setCanceled(true);
				}
			}
		}
	}
}
