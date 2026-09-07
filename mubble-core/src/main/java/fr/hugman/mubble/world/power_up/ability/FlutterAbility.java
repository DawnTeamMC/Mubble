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
 * The rising half of a jump held on: past the peak of it, a holder still leaning on the jump key climbs
 * again for a moment instead of falling.
 * <p>
 * The climb builds rather than holding one speed — the holder is pushed a little harder on every tick of it,
 * so the flutter starts as a hesitation and ends as a proper lift. That is what tells it apart from a second
 * jump, which would hand over all of its height at once.
 * <p>
 * What happens once the climb is over is not this ability's business: see {@link FloatAbility}, which a form
 * is free to grant on its own.
 *
 * @param duration     how many ticks the climb lasts at most
 * @param speed        the upward speed the climb opens on, in blocks per tick
 * @param acceleration how much speed every tick of the climb adds to it, in blocks per tick per tick
 * @param sound        the sound played in loop for as long as the climb lasts
 * @param particle     the particle left around the feet of the holder while they climb
 */
public record FlutterAbility(
        int duration,
        float speed,
        float acceleration,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle
) {
    public FlutterAbility {
        // A data pack is free to write anything; what it cannot do is send the holder downwards on an
        // ability whose whole point is to hold them up.
        duration = Math.max(0, duration);
        speed = Math.max(0.0F, speed);
        acceleration = Math.max(0.0F, acceleration);
    }

    public static final int DEFAULT_DURATION = 20;
    public static final float DEFAULT_SPEED = 0.05F;
    public static final float DEFAULT_ACCELERATION = 0.005F;

    public static final Codec<FlutterAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("duration", DEFAULT_DURATION).forGetter(FlutterAbility::duration),
            Codec.FLOAT.optionalFieldOf("speed", DEFAULT_SPEED).forGetter(FlutterAbility::speed),
            Codec.FLOAT.optionalFieldOf("acceleration", DEFAULT_ACCELERATION).forGetter(FlutterAbility::acceleration),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(FlutterAbility::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(FlutterAbility::particle)
    ).apply(instance, FlutterAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlutterAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FlutterAbility::duration,
            ByteBufCodecs.FLOAT, FlutterAbility::speed,
            ByteBufCodecs.FLOAT, FlutterAbility::acceleration,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), FlutterAbility::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), FlutterAbility::particle,
            FlutterAbility::new
    );

    /**
     * A flutter with nothing to see or hear.
     */
    public static FlutterAbility of(int duration, float speed, float acceleration) {
        return new FlutterAbility(duration, speed, acceleration, Optional.empty(), Optional.empty());
    }

    /**
     * The upward speed the flutter is worth on one of its ticks.
     *
     * @param elapsed how many ticks the flutter has already run, the first one being 0
     * @return the upward speed for that tick, in blocks per tick
     */
    public float liftAt(int elapsed) {
        return this.speed + this.acceleration * elapsed;
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
