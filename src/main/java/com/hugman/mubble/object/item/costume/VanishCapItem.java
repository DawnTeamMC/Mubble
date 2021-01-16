package com.hugman.mubble.object.item.costume;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

public class VanishCapItem extends HatItem {
	public VanishCapItem(Item.Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}

	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if(player.isSneaking()) player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 19, 0));
	}
}