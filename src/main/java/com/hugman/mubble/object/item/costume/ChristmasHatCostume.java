package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.util.CalendarEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

import java.util.Random;

public class ChristmasHatCostume extends HeadCostume {
	public ChristmasHatCostume(Item.Settings builder, SoundEvent sound) {
		super(builder, sound);
	}

	@Override
	public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks) {
		Random rand = new Random();
		if(world.isClient && rand.nextInt(2) == 0 && CalendarEvents.isDecember) {
			world.addParticle(ParticleTypes.ITEM_SNOWBALL, player.getX() + (rand.nextDouble() - 0.5D) * 0.3D, player.getY() + player.getHeight() + rand.nextDouble() * 0.3D, player.getZ() + (rand.nextDouble() - 0.5D) * 0.3D, (rand.nextDouble() - 0.5D) * 1.1D, (rand.nextDouble() - 0.5D) * 1.1D, (rand.nextDouble() - 0.5D) * 1.1D);
		}
		super.usageTick(world, player, stack, remainingUseTicks);
	}
}