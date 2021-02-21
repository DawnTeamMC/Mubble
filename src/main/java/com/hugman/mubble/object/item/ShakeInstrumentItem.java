package com.hugman.mubble.object.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ShakeInstrumentItem extends InstrumentItem {
	public ShakeInstrumentItem(Item.Settings builder, SoundEvent soundIn) {
		super(builder, soundIn);
	}

	@Override
	public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand handIn) {
		player.swingHand(handIn);
		player.playSound(getInstrumentSound(), 0.5F, player.getPitch(1.0F) / -90.0F + 1.0F);
		player.incrementStat(Stats.USED.getOrCreateStat(this));
		return TypedActionResult.success(player.getStackInHand(handIn));
	}
}
