package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;

public class CappyItem extends HatItem {
	public CappyItem(Settings builder) {
		super(builder, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, false);
	}

	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		Random rand = new Random();
		World world = player.getEntityWorld();
		if(!world.isClient && rand.nextInt(301) == 0) {
			int random = rand.nextInt(5);
			if(world.getDimension().isUltrawarm() && random <= 3) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.COSTUME_CAPPY_AMBIENT_NETHER, SoundCategory.VOICE, 1f, 1f);
			}
			else {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.COSTUME_CAPPY_AMBIENT, SoundCategory.VOICE, 1f, 1f);
			}
		}
	}

	@Override
	public void onEquip(PlayerEntity player, ItemStack stack) {
		player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.COSTUME_CAPPY_EQUIP, SoundCategory.PLAYERS, 1f, 1f);
		super.onEquip(player, stack);
	}
}