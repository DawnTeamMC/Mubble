package com.hugman.mubble.object.item.costume;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class WingCapItem extends HatItem {
	public WingCapItem(Item.Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == Items.FEATHER;
	}

	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if(isUsable(stack) && player.isSprinting()) {
			stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 1, 2));
			player.fallDistance = 0f;
		}
	}
}