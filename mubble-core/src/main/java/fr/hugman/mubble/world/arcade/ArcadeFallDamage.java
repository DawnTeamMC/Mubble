package fr.hugman.mubble.world.arcade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * How a profile treats fall damage. A ground pound landing and a roll out of a landing negate it
 * whatever these say.
 *
 * @param multiplier         scale of the fall damage taken
 * @param safeDistanceBonus  extra blocks a player may fall without damage, on top of the vanilla
 *                           safe fall distance
 */
public record ArcadeFallDamage(double multiplier, double safeDistanceBonus) {
    public static final Codec<ArcadeFallDamage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.doubleRange(0.0D, 16.0D).fieldOf("multiplier").forGetter(ArcadeFallDamage::multiplier),
            Codec.doubleRange(0.0D, 256.0D).fieldOf("safe_distance_bonus").forGetter(ArcadeFallDamage::safeDistanceBonus)
    ).apply(instance, ArcadeFallDamage::new));
}
