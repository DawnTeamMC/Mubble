package fr.hugman.mubble.world.power_up.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

/**
 * Extends a jump by fluttering: past the peak of it, a holder still leaning on the jump key rises again
 * for a moment instead of falling.
 * <p>
 * Everything the flutter is worth lives here rather than in whichever form happens to grant it, so that the
 * next form to want one only has to hand over its own numbers. The lift is not handed out whole on the first
 * tick either: it climbs over {@link #ramp} ticks, which is what tells a flutter apart from a second jump.
 *
 * @param duration how many ticks a flutter lasts at most
 * @param ramp     how many ticks the lift takes to reach its full strength
 * @param strength the upward speed a flutter is worth once ramped up, in blocks per tick
 * @param sound    the sound played in loop for as long as the flutter lasts
 * @param particle the particle left around the feet of the holder while they flutter
 */
public record FlutterAbility(
        int duration,
        int ramp,
        float strength,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle
) {
    public FlutterAbility {
        // A data pack is free to write anything; what it cannot do is send the holder downwards on an
        // ability whose whole point is to hold them up.
        duration = Math.max(0, duration);
        ramp = Math.max(0, ramp);
        strength = Math.max(0.0F, strength);
    }

    public static final int DEFAULT_DURATION = 20;
    public static final int DEFAULT_RAMP = 5;
    public static final float DEFAULT_STRENGTH = 0.12F;

    public static final Codec<FlutterAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("duration", DEFAULT_DURATION).forGetter(FlutterAbility::duration),
            Codec.INT.optionalFieldOf("ramp", DEFAULT_RAMP).forGetter(FlutterAbility::ramp),
            Codec.FLOAT.optionalFieldOf("strength", DEFAULT_STRENGTH).forGetter(FlutterAbility::strength),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(FlutterAbility::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(FlutterAbility::particle)
    ).apply(instance, FlutterAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlutterAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FlutterAbility::duration,
            ByteBufCodecs.INT, FlutterAbility::ramp,
            ByteBufCodecs.FLOAT, FlutterAbility::strength,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), FlutterAbility::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), FlutterAbility::particle,
            FlutterAbility::new
    );

    /**
     * A flutter on the default numbers, with nothing to see or hear.
     */
    public static FlutterAbility of(int duration, int ramp, float strength) {
        return new FlutterAbility(duration, ramp, strength, Optional.empty(), Optional.empty());
    }

    /**
     * The upward speed the flutter is worth on one of its ticks.
     * <p>
     * The first tick already lifts a little: a flutter that started with nothing would let the holder keep
     * falling for as long as the ramp lasts, which reads as the jump key being ignored.
     *
     * @param elapsed how many ticks the flutter has already run, the first one being 0
     * @return the upward speed for that tick, in blocks per tick
     */
    public float liftAt(int elapsed) {
        if (this.ramp <= 0) {
            return this.strength;
        }
        return this.strength * Math.min(1.0F, (float) (elapsed + 1) / (float) this.ramp);
    }

    /**
     * How high a whole flutter carries its holder, gravity left aside.
     */
    public float totalLift() {
        float total = 0.0F;
        for (int tick = 0; tick < this.duration; tick++) {
            total += this.liftAt(tick);
        }
        return total;
    }
}
