package fr.hugman.mubble.world.power_up.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Holding the jump key in mid-air flies, for as long as a gauge lasts.
 *
 * @param gauge        how many ticks of flight a full gauge holds
 * @param restart      how many ticks an emptied gauge has to be back to before the holder can take off again
 * @param lift         the vertical speed the flight settles on, in blocks per tick
 * @param acceleration how much the vertical speed moves towards the lift every tick, in blocks per tick per tick
 * @param airControl   what the air steering of the holder is multiplied by while flying
 * @param fallDamage   the share of the fall distance a tick of flight leaves, from 0 to 1
 * @param sputter      how many of the last ticks of the gauge the lift stutters for
 * @param refills      how many ticks each refill source puts back into the gauge
 * @param bar          the sprites of the gauge on the HUD, vanilla's jump bar when absent
 */
public record FlightAbility(
        int gauge,
        int restart,
        float lift,
        float acceleration,
        float airControl,
        float fallDamage,
        int sputter,
        Map<Identifier, Integer> refills,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle,
        Optional<ParticleOptions> sputterParticle,
        Optional<Bar> bar
) {
    public static final Identifier GROUND = Mubble.id("ground");
    public static final Identifier CLIMBING = Mubble.id("climbing");
    public static final Identifier CLINGING = Mubble.id("clinging");

    public static final int DEFAULT_GAUGE = 100;
    public static final int DEFAULT_RESTART = 20;
    public static final float DEFAULT_LIFT = 0.25F;
    public static final float DEFAULT_ACCELERATION = 0.1F;
    public static final float DEFAULT_AIR_CONTROL = 1.5F;
    public static final float DEFAULT_FALL_DAMAGE = 0.0F;
    public static final int DEFAULT_SPUTTER = 6;

    public FlightAbility {
        gauge = Math.max(1, gauge);
        restart = Mth.clamp(restart, 1, gauge);
        acceleration = Math.max(0.0F, acceleration);
        airControl = Math.max(0.0F, airControl);
        fallDamage = Mth.clamp(fallDamage, 0.0F, 1.0F);
        sputter = Math.max(0, sputter);
        refills = Map.copyOf(refills);
    }

    public static final Codec<FlightAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("gauge", DEFAULT_GAUGE).forGetter(FlightAbility::gauge),
            Codec.INT.optionalFieldOf("restart", DEFAULT_RESTART).forGetter(FlightAbility::restart),
            Codec.FLOAT.optionalFieldOf("lift", DEFAULT_LIFT).forGetter(FlightAbility::lift),
            Codec.FLOAT.optionalFieldOf("acceleration", DEFAULT_ACCELERATION).forGetter(FlightAbility::acceleration),
            Codec.FLOAT.optionalFieldOf("air_control", DEFAULT_AIR_CONTROL).forGetter(FlightAbility::airControl),
            Codec.FLOAT.optionalFieldOf("fall_damage", DEFAULT_FALL_DAMAGE).forGetter(FlightAbility::fallDamage),
            Codec.INT.optionalFieldOf("sputter", DEFAULT_SPUTTER).forGetter(FlightAbility::sputter),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).optionalFieldOf("refills", Map.of()).forGetter(FlightAbility::refills),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(FlightAbility::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(FlightAbility::particle),
            ParticleTypes.CODEC.optionalFieldOf("sputter_particle").forGetter(FlightAbility::sputterParticle),
            Bar.CODEC.optionalFieldOf("bar").forGetter(FlightAbility::bar)
    ).apply(instance, FlightAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FlightAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, FlightAbility::gauge,
            ByteBufCodecs.VAR_INT, FlightAbility::restart,
            ByteBufCodecs.FLOAT, FlightAbility::lift,
            ByteBufCodecs.FLOAT, FlightAbility::acceleration,
            ByteBufCodecs.FLOAT, FlightAbility::airControl,
            ByteBufCodecs.FLOAT, FlightAbility::fallDamage,
            ByteBufCodecs.VAR_INT, FlightAbility::sputter,
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.VAR_INT), FlightAbility::refills,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), FlightAbility::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), FlightAbility::particle,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), FlightAbility::sputterParticle,
            Bar.STREAM_CODEC.apply(ByteBufCodecs::optional), FlightAbility::bar,
            FlightAbility::new
    );

    public static FlightAbility of(int gauge, int restart, Map<Identifier, Integer> refills) {
        return new FlightAbility(gauge, restart, DEFAULT_LIFT, DEFAULT_ACCELERATION, DEFAULT_AIR_CONTROL, DEFAULT_FALL_DAMAGE, DEFAULT_SPUTTER,
                refills, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public int refillFor(Identifier source) {
        return Math.max(0, this.refills.getOrDefault(source, 0));
    }

    /**
     * @param speed the vertical speed of the tick before
     */
    public float speedAfter(float speed) {
        return Mth.approach(speed, this.lift, this.acceleration);
    }

    /**
     * @param left the gauge left once this tick is spent
     * @return the vertical speed this tick actually gets, the sputter hanging every other tick
     */
    public float liftFor(float speed, int left) {
        return this.isSputtering(left) && left % 2 == 1 ? Math.min(speed, 0.0F) : speed;
    }

    public boolean isSputtering(int left) {
        return left < this.sputter;
    }

    public record Bar(Identifier background, Identifier progress) {
        public static final Codec<Bar> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("background").forGetter(Bar::background),
                Identifier.CODEC.fieldOf("progress").forGetter(Bar::progress)
        ).apply(instance, Bar::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Bar> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, Bar::background,
                Identifier.STREAM_CODEC, Bar::progress,
                Bar::new
        );
    }
}
