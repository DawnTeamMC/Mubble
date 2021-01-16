package com.hugman.mubble.object.item.costume;

import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class WearableItem extends TrinketItem {
	protected final SoundEvent equipSound;
	protected final String group;
	protected final String slot;

	public WearableItem(Settings settings, SoundEvent equipSound, String group, String slot) {
		super(settings);
		this.equipSound = equipSound;
		this.group = group;
		this.slot = slot;
	}

	@Override
	public void onEquip(PlayerEntity player, ItemStack stack) {
		player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(), this.equipSound, SoundCategory.PLAYERS, 1f, 1f);
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(this.group) && slot.equals(this.slot);
	}

	public static boolean isUsable(ItemStack stack) {
		return stack.getDamage() < stack.getMaxDamage() - 1;
	}
}
