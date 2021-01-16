package com.hugman.mubble.object.item.costume;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

public class GuardianMaskCostume extends MaskItem {
	public GuardianMaskCostume(Item.Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}

	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if(player.isSneaking() && player.getItemCooldownManager().getCooldownProgress(this, 0) == 0) {
			player.getItemCooldownManager().set(this, 25);
			player.incrementStat(Stats.USED.getOrCreateStat(this));
		}
	}
}