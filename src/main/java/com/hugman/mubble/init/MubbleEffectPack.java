package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.EffectCreator;
import com.hugman.mubble.object.effect.HiccupEffect;
import com.hugman.mubble.object.effect.SimpleEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class MubbleEffectPack extends MubblePack {
	public static final StatusEffect HEAVINESS = register(new EffectCreator.Builder("heaviness", new SimpleEffect(StatusEffectType.HARMFUL, 9198906)));
	public static final StatusEffect HICCUP = register(new EffectCreator.Builder("hiccup", new HiccupEffect(StatusEffectType.NEUTRAL, 9198906)));
}