package com.hugman.mubble.object.item;

import com.hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class SmashBallItem extends Item {
	public SmashBallItem(Item.Settings builder) {
		super(builder);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		Random rand = player.getRandom();
		world.playSound(null, player.getBlockPos(), MubbleSounds.ITEM_SMASH_BALL_USE, SoundCategory.PLAYERS, 1f, 1f);
		for(int i = 0; i < rand.nextInt(21) + 10; i++) {
			world.addParticle(ParticleTypes.FLAME, player.getX() + (rand.nextInt(11) - 5) / 10F, player.getY() + rand.nextInt(21) / 10F, player.getZ() + (rand.nextInt(11) - 5) / 10F, (rand.nextInt(21) - 10) / 120F, (rand.nextInt(2) + 0.1) / 11F, (rand.nextInt(21) - 10) / 120F);
		}
		player.getVelocity().add(0.0D, 0.25, 0.0D);
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 25, 0));
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 25, 0));
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 900, 3));
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 900, 4));
		if(!player.abilities.creativeMode) {
			stack.decrement(1);
		}
		player.getItemCooldownManager().set(this, 25);
		player.incrementStat(Stats.USED.getOrCreateStat(this));
		return TypedActionResult.success(stack);
	}
}
