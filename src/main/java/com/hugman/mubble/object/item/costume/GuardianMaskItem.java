package com.hugman.mubble.object.item.costume;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

public class GuardianMaskItem extends MaskItem {
	public GuardianMaskItem(Item.Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}

	@Override
	public int getAbilityCooldown(PlayerEntity player, ItemStack stack) {
		return 30;
	}

	@Override
	public void onAbilityUsage(PlayerEntity player, ItemStack stack) {
		if(!player.getEntityWorld().isClient()) {
			stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
		}
	}
}