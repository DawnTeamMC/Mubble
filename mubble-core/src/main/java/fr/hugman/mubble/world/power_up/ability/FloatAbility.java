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
 * The falling half of a jump held on: a holder still leaning on the jump key comes down at a walking pace
 * instead of dropping, and is spared most of what the fall would otherwise be worth.
 * <p>
 * It only ever slows a fall down: it never pushes anyone up, and it leaves a holder who is still climbing
 * alone. That is what makes it worth having on its own — a form granting nothing but this one floats down
 * from wherever its jump took it, with no climb of its own to speak of first.
 * <p>
 * Unlike the {@link FlutterAbility} that usually comes before it, it lasts as long as the key is held rather
 * than for a set number of ticks, and letting go of the key only ends it until it is pressed again.
 *
 * @param speed      the speed the holder comes down at, in blocks per tick
 * @param fallDamage the share of a floated fall that still counts for fall damage, from 0 to 1
 * @param sound      the sound played in loop for as long as the float lasts
 * @param particle   the particle left around the feet of the holder while they float
 * @param airControl the steering the holder gets while they float, a plain fall's when absent
 */
public record FloatAbility(
        float speed,
        float fallDamage,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle,
        Optional<AirControl> airControl
) {
    public FloatAbility {
        // A negative speed would send a floating holder upwards, which is the other ability's job.
        speed = Math.max(0.0F, speed);
        fallDamage = Mth.clamp(fallDamage, 0.0F, 1.0F);
    }

    public static final float DEFAULT_SPEED = 0.25F;
    public static final float DEFAULT_FALL_DAMAGE = 0.5F;

    public static final Codec<FloatAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("speed", DEFAULT_SPEED).forGetter(FloatAbility::speed),
            Codec.FLOAT.optionalFieldOf("fall_damage", DEFAULT_FALL_DAMAGE).forGetter(FloatAbility::fallDamage),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(FloatAbility::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(FloatAbility::particle),
            AirControl.CODEC.optionalFieldOf("air_control").forGetter(FloatAbility::airControl)
    ).apply(instance, FloatAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FloatAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, FloatAbility::speed,
            ByteBufCodecs.FLOAT, FloatAbility::fallDamage,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), FloatAbility::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), FloatAbility::particle,
            AirControl.STREAM_CODEC.apply(ByteBufCodecs::optional), FloatAbility::airControl,
            FloatAbility::new
    );

    /**
     * A float with nothing to see or hear.
     */
    public static FloatAbility of(float speed, float fallDamage) {
        return new FloatAbility(speed, fallDamage, Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * How much of a tick spent floating the holder is spared on landing.
     *
     * @return the blocks to take off the fall the holder has built up, for one tick of floating
     */
    public float fallForgivenPerTick() {
        return this.speed * (1.0F - this.fallDamage);
    }

    /**
     * @param acceleration the horizontal speed the holder can add every tick on top of a plain fall's, in blocks per tick
     * @param topSpeed     the horizontal speed steering stops adding at, in blocks per tick
     */
    public record AirControl(float acceleration, float topSpeed) {
        public AirControl {
            acceleration = Math.max(0.0F, acceleration);
            topSpeed = Math.max(0.0F, topSpeed);
        }

        public static final float DEFAULT_ACCELERATION = 0.04F;
        public static final float DEFAULT_TOP_SPEED = 0.35F;

        public static final Codec<AirControl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.optionalFieldOf("acceleration", DEFAULT_ACCELERATION).forGetter(AirControl::acceleration),
                Codec.FLOAT.optionalFieldOf("top_speed", DEFAULT_TOP_SPEED).forGetter(AirControl::topSpeed)
        ).apply(instance, AirControl::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, AirControl> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, AirControl::acceleration,
                ByteBufCodecs.FLOAT, AirControl::topSpeed,
                AirControl::new
        );
    }
}
