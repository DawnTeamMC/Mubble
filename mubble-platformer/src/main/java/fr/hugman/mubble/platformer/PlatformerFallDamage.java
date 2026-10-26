package fr.hugman.mubble.platformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * How a profile treats fall damage, through the attributes of vanilla while the profile is active (see
 * {@link PlatformerAttributes#PROFILE}). A ground pound landing and a roll out of a landing negate it
 * whatever these say.
 *
 * @param multiplier         scale of the fall damage taken, on {@code minecraft:fall_damage_multiplier}
 * @param safeDistanceBonus  extra blocks a player may fall without damage, added to
 *                           {@code minecraft:safe_fall_distance}
 */
public record PlatformerFallDamage(double multiplier, double safeDistanceBonus) {
    public static final Codec<PlatformerFallDamage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.doubleRange(0.0D, 16.0D).fieldOf("multiplier").forGetter(PlatformerFallDamage::multiplier),
            Codec.doubleRange(0.0D, 256.0D).fieldOf("safe_distance_bonus").forGetter(PlatformerFallDamage::safeDistanceBonus)
    ).apply(instance, PlatformerFallDamage::new));
}
