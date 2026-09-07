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
import net.minecraft.util.Mth;

import java.util.Optional;

/**
 * The rising half of a jump held on: past the peak of it, a holder still leaning on the jump key climbs
 * again for a moment instead of falling.
 * <p>
 * It opens on a drop rather than on a lift. For the first few ticks the holder is still sinking, only less
 * and less of it, and then the same acceleration that ate the drop carries them up hard. Those few ticks of
 * hanging are what make the lift that follows read as a snap rather than as a balloon, so the whole thing is
 * over in well under a second — a flutter that lifted from its very first tick would just be a second jump.
 * <p>
 * What happens once the climb is over is not this ability's business: see {@link FloatAbility}, which a form
 * is free to grant on its own.
 *
 * @param duration     how many ticks the climb lasts at most
 * @param drop         how fast the holder is still sinking when it opens, in blocks per tick
 * @param acceleration how much every tick takes off that drop, and then adds to the lift, in blocks per tick
 *                     per tick
 * @param sound        the sound played in loop for as long as the climb lasts
 * @param particle     the particle left around the feet of the holder while they climb
 */
public record FlutterAbility(
        int duration,
        float drop,
        float acceleration,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle
) {
    public FlutterAbility {
        // A data pack is free to write anything; what it cannot do is turn the numbers around, on an ability
        // whose whole point is to have the holder sink and then climb rather than the other way about.
        duration = Math.max(0, duration);
        drop = Math.max(0.0F, drop);
        acceleration = Math.max(0.0F, acceleration);
    }

    public static final int DEFAULT_DURATION = 8;
    public static final float DEFAULT_DROP = 0.2F;
    public static final float DEFAULT_ACCELERATION = 0.1F;

    public static final Codec<FlutterAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("duration", DEFAULT_DURATION).forGetter(FlutterAbility::duration),
            Codec.FLOAT.optionalFieldOf("drop", DEFAULT_DROP).forGetter(FlutterAbility::drop),
            Codec.FLOAT.optionalFieldOf("acceleration", DEFAULT_ACCELERATION).forGetter(FlutterAbility::acceleration),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(FlutterAbility::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(FlutterAbility::particle)
    ).apply(instance, FlutterAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlutterAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FlutterAbility::duration,
            ByteBufCodecs.FLOAT, FlutterAbility::drop,
            ByteBufCodecs.FLOAT, FlutterAbility::acceleration,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), FlutterAbility::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), FlutterAbility::particle,
            FlutterAbility::new
    );

    /**
     * A flutter with nothing to see or hear.
     */
    public static FlutterAbility of(int duration, float drop, float acceleration) {
        return new FlutterAbility(duration, drop, acceleration, Optional.empty(), Optional.empty());
    }

    /**
     * The vertical speed the flutter is worth on one of its ticks, negative while the holder is still sinking
     * and positive once the acceleration has turned that around.
     *
     * @param elapsed how many ticks the flutter has already run, the first one being 0
     * @return the speed for that tick, in blocks per tick, upwards when positive
     */
    public float liftAt(int elapsed) {
        return this.acceleration * elapsed - this.drop;
    }

    /**
     * How many ticks the holder keeps sinking for before the flutter starts carrying them up.
     */
    public int hangTicks() {
        if (this.acceleration <= 0.0F) {
            return this.duration;
        }
        return Math.min(this.duration, Mth.ceil(this.drop / this.acceleration));
    }

    /**
     * The net height a whole flutter is worth, the drop it opens on included and gravity left aside.
     */
    public float totalLift() {
        float total = 0.0F;
        for (int tick = 0; tick < this.duration; tick++) {
            total += this.liftAt(tick);
        }
        return total;
    }
}
