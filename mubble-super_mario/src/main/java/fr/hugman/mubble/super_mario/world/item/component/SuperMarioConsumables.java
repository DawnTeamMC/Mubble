package fr.hugman.mubble.super_mario.world.item.component;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;

public class SuperMarioConsumables {
    public static final Consumable SUPER_MUSHROOM = Consumable.builder()
            .consumeSeconds(0.0f)
            .animation(ItemUseAnimation.NONE)
            .hasConsumeParticles(false)
            .onConsume(
                    new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 1)))
            )
            .build();
}
