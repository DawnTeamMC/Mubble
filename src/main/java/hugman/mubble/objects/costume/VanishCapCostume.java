package hugman.mubble.objects.costume;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class VanishCapCostume extends HeadCostume
{
	public VanishCapCostume(Item.Settings builder)
	{
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}

	@Override
	public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks)
	{
		if (!world.isClient && player.isSneaking())
		{
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 2, 0));
		}
		super.usageTick(world, player, stack, remainingUseTicks);
	}
}