/*package hugman.mubble.objects.events_handler;

import hugman.mubble.init.MubbleEnchantments;
import hugman.mubble.util.EnchantmentUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class EnchantmentHandler
{
	public static void onEntityDropsItems(LivingDropsEvent event)
	{
		Entity sourceEntity = event.getSource().getTrueSource();
		if(sourceEntity != null)
		{
			if(sourceEntity instanceof PlayerEntity)
			{
				PlayerEntity playerIn = (PlayerEntity)sourceEntity;
				if(EnchantmentUtil.hasEnchantment(MubbleEnchantments.TELEKINESIS, playerIn.getStackInHand(Hand.MAIN_HAND)))
				{
					for(ItemEntity itemEntity : event.getDrops())
					{
						ItemStack itemStack = itemEntity.getStack();
						playerIn.inventory.insertStack(itemStack);
					}
					event.setCanceled(true);
				}
			}
		}
	}
	
	public static void onEntityDropsExperience(LivingExperienceDropEvent event)
	{
		if(event.getAttackingPlayer() != null)
		{
			PlayerEntity playerIn = event.getAttackingPlayer();
			if(playerIn.getStackInHand(Hand.MAIN_HAND) != null)
			{
				if(EnchantmentUtil.hasEnchantment(MubbleEnchantments.TELEKINESIS, playerIn.getStackInHand(Hand.MAIN_HAND)) >= 2)
				{
					playerIn.giveExperiencePoints(event.getDroppedExperience());
					event.setCanceled(true);
				}
			}
		}
	}
}*/
