package hugman.mubble.objects.item.costume;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

public class GuardianMaskCostume extends HeadCostume
{
	public GuardianMaskCostume(Item.Settings builder)
	{
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}

	@Override
	public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks)
	{
		if (player.isSneaking() && ((PlayerEntity) player).getItemCooldownManager().getCooldownProgress(this, 0) == 0)
		{
			((PlayerEntity) player).getItemCooldownManager().set(this, 25);
			((PlayerEntity) player).incrementStat(Stats.USED.getOrCreateStat(this));
		}
		super.usageTick(world, player, stack, remainingUseTicks);
	}
}