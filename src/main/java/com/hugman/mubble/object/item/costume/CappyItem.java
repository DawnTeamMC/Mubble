package com.hugman.mubble.object.item.costume;

import com.hugman.mubble.init.MubbleSounds;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class CappyItem extends HatItem {
	public CappyItem(Settings builder, SoundEvent sound) {
		super(builder, sound);
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
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if(Trinket.equipTrinket(player, hand).getResult() == ActionResult.SUCCESS) {
			world.playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.COSTUME_CAPPY_EQUIP, SoundCategory.PLAYERS, 1f, 1f);
		}
		return super.use(world, player, hand);
	}
}