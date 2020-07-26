package com.hugman.mubble.init;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.object.effect.HiccupEffect;
import com.hugman.mubble.object.effect.SimpleEffect;
import com.hugman.mubble.util.DataWriter;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleStatusEffects {
	private static StatusEffect register(String name, StatusEffect statusEffect) {
		DataWriter.statusEffectsEntries.add(Mubble.id(name));
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(Mubble.MOD_ID, name), statusEffect);
	}

	public static final StatusEffect HEAVINESS = register("heaviness", new SimpleEffect(StatusEffectType.HARMFUL, 9198906));
	//public static final StatusEffect SNEEZING = new SimpleEffect("sneezing", true, 9753716);
	public static final StatusEffect HICCUP = register("hiccup", new HiccupEffect(StatusEffectType.NEUTRAL, 9198906));
}