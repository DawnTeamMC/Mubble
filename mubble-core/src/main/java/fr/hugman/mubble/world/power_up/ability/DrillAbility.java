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
 * A fast spinning descent started by a fresh press of sneak in the air, ending on landing.
 *
 * @param speed      the speed the holder comes down at, in blocks per tick
 * @param control    the share of their horizontal speed the holder keeps from one tick to the next, from 0 to 1
 * @param fallDamage the share of the drilled distance that still counts for fall damage, from 0 to 1
 * @param sound      the sound played in loop while drilling
 * @param landSound  the sound played on landing
 */
public record DrillAbility(
        float speed,
        float control,
        float fallDamage,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle,
        Optional<Holder<SoundEvent>> landSound
) {
    public DrillAbility {
        speed = Math.max(0.0F, speed);
        control = Mth.clamp(control, 0.0F, 1.0F);
        fallDamage = Mth.clamp(fallDamage, 0.0F, 1.0F);
    }

    public static final float DEFAULT_SPEED = 1.0F;
    public static final float DEFAULT_CONTROL = 0.3F;
    public static final float DEFAULT_FALL_DAMAGE = 0.0F;

    public static final Codec<DrillAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("speed", DEFAULT_SPEED).forGetter(DrillAbility::speed),
            Codec.FLOAT.optionalFieldOf("control", DEFAULT_CONTROL).forGetter(DrillAbility::control),
            Codec.FLOAT.optionalFieldOf("fall_damage", DEFAULT_FALL_DAMAGE).forGetter(DrillAbility::fallDamage),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(DrillAbility::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(DrillAbility::particle),
            SoundEvent.CODEC.optionalFieldOf("land_sound").forGetter(DrillAbility::landSound)
    ).apply(instance, DrillAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DrillAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, DrillAbility::speed,
            ByteBufCodecs.FLOAT, DrillAbility::control,
            ByteBufCodecs.FLOAT, DrillAbility::fallDamage,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), DrillAbility::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), DrillAbility::particle,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), DrillAbility::landSound,
            DrillAbility::new
    );

    public static DrillAbility of(float speed, float control, float fallDamage) {
        return new DrillAbility(speed, control, fallDamage, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public float fallForgivenPerTick() {
        return this.speed * (1.0F - this.fallDamage);
    }
}
