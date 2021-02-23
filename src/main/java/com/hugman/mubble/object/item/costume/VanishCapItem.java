package com.hugman.mubble.object.item.costume;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

public class VanishCapItem extends HatItem {
	public VanishCapItem(Item.Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false);
	}

	@Override
	public int getAbilityCooldown(PlayerEntity player, ItemStack stack) {
		return 10;
	}

	@Override
	public void onAbilityUsage(PlayerEntity player, ItemStack stack) {
		if(!player.getEntityWorld().isClient()) {
			stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, (int) (getAbilityCooldown(player, stack) * 1.5D), 0, false, false));
		}
	}
}