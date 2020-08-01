package com.hugman.mubble.object.item;

import com.hugman.mubble.init.MubbleSoundPack;
import com.hugman.mubble.init.data.MubbleItemTiers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class LightsaberItem extends SwordItem {
	public static int idleTimer = 0;

	public LightsaberItem(Settings builder) {
		super(MubbleItemTiers.KYBER, 3, -2.4F, builder);
	}

	public void onSwing(LivingEntity entity, boolean hitsEntity) {
		entity.playSound(MubbleSoundPack.ITEM_LIGHTSABER_SWIPE, 1.0F, 1.0F);
		if(hitsEntity) {
			entity.playSound(MubbleSoundPack.ITEM_LIGHTSABER_HIT, 1.0F, 1.0F);
		}
	}

	public void onPullOut(Entity entity, World world) {
		world.playSoundFromEntity(null, entity, MubbleSoundPack.ITEM_LIGHTSABER_PULL_OUT, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}

	public void onPullIn(Entity entity, World world) {
		world.playSoundFromEntity(null, entity, MubbleSoundPack.ITEM_LIGHTSABER_PULL_IN, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if(entity instanceof ServerPlayerEntity) {
			StopSoundS2CPacket packet = new StopSoundS2CPacket(MubbleSoundPack.ITEM_LIGHTSABER_IDLE.getId(), SoundCategory.MASTER);
			((ServerPlayerEntity) entity).networkHandler.sendPacket(packet);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(selected && world.isClient && idleTimer == 0 && entity instanceof PlayerEntity) {
			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(MubbleSoundPack.ITEM_LIGHTSABER_IDLE, 1.0F, 0.15F));
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
}