package com.hugman.mubble.object.item.costume;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class GooigiCapItem extends HatItem {
	public GooigiCapItem(Settings builder) {
		super(builder, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, false);
	}

	@Override
	public int getAbilityCooldown(PlayerEntity player, ItemStack stack) {
		return 50;
	}

	@Override
	public void onAbilityUsage(PlayerEntity player, ItemStack stack) {
		World world = player.getEntityWorld();
		if(!world.isClient()) {
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_SLIME_BLOCK_HIT, SoundCategory.PLAYERS, 1f, 1f);
			stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, getAbilityCooldown(player, stack), player.getRandom().nextInt(3)));
			player.fallDistance = 0f;
		}
	}
}