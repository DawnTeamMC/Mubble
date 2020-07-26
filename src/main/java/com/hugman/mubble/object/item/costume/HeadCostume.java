package com.hugman.mubble.object.item.costume;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class HeadCostume extends Costume {
	public HeadCostume(Settings builder, SoundEvent sound, StatusEffectInstance... potionEffects) {
		super(builder, sound, EquipmentSlot.HEAD, potionEffects);
	}

	public HeadCostume(Settings builder, SoundEvent sound, Identifier shader, StatusEffectInstance... potionEffects) {
		super(builder, sound, EquipmentSlot.HEAD, shader, potionEffects);
	}
}