package com.hugman.mubble.object.item.costume;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;

public class GooigiCapItem extends HatItem {
	public GooigiCapItem(Settings builder) {
		super(builder, SoundEvents.BLOCK_SLIME_BLOCK_PLACE, false);
	}

	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		Random rand = new Random();
		World world = player.getEntityWorld();
		if(!world.isClient && rand.nextInt(51) == 0) {
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_SLIME_BLOCK_HIT, SoundCategory.PLAYERS, 1f, 1f);
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, (rand.nextInt(3) + 1) * 20, rand.nextInt(3)));
		}
	}
}