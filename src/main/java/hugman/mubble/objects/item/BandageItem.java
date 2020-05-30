package hugman.mubble.objects.item;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BandageItem extends Item
{
	public static final List<StatusEffect> CURABLE_EFFECTS = new ArrayList<>(Arrays.asList(StatusEffects.MINING_FATIGUE, StatusEffects.NAUSEA, StatusEffects.POISON, StatusEffects.WITHER));

	public BandageItem(Item.Settings builder)
	{
		super(builder);
	}

	@Override
	public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand handIn)
	{
		ItemStack stack = player.getStackInHand(handIn);
		boolean doesCure = false;
		for (StatusEffect effect : CURABLE_EFFECTS)
		{
			if (player.hasStatusEffect(effect))
			{
				doesCure = true;
				player.removeStatusEffect(effect);
			}
		}
		if (doesCure)
		{
			worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 0.5F, 1F);
			if (!player.abilities.creativeMode)
			{
				stack.decrement(1);
			}
			player.incrementStat(Stats.USED.getOrCreateStat(this));
			return TypedActionResult.success(stack);
		}
		return TypedActionResult.fail(stack);
	}
}
