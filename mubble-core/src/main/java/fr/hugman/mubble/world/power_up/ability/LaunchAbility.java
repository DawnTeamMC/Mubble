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
 * A boost straight up on a double tap of the jump key, once per ground contact.
 *
 * @param doubleTapWindow how many ticks the second tap may come after the first
 * @param height          how high the boost carries the holder, in blocks
 * @param slowFall        the descent the holder is held to from the peak of the boost on
 */
public record LaunchAbility(
        int doubleTapWindow,
        float height,
        Optional<Holder<SoundEvent>> sound,
        Optional<ParticleOptions> particle,
        Optional<FloatAbility> slowFall
) {
    public LaunchAbility {
        doubleTapWindow = Math.max(1, doubleTapWindow);
        height = Math.max(0.0F, height);
    }

    /** Vanilla's own window for the double tap that toggles creative flight. */
    public static final int DEFAULT_DOUBLE_TAP_WINDOW = 7;
    public static final float DEFAULT_HEIGHT = 7.0F;
    /** What vanilla keeps of a vertical speed from one tick to the next in the air. */
    public static final double AIR_DRAG = 0.98D;
    private static final int MAX_TICKS = 1000;

    public static final Codec<LaunchAbility> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("double_tap_window", DEFAULT_DOUBLE_TAP_WINDOW).forGetter(LaunchAbility::doubleTapWindow),
            Codec.FLOAT.optionalFieldOf("height", DEFAULT_HEIGHT).forGetter(LaunchAbility::height),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(LaunchAbility::sound),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(LaunchAbility::particle),
            FloatAbility.CODEC.optionalFieldOf("slow_fall").forGetter(LaunchAbility::slowFall)
    ).apply(instance, LaunchAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LaunchAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, LaunchAbility::doubleTapWindow,
            ByteBufCodecs.FLOAT, LaunchAbility::height,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), LaunchAbility::sound,
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), LaunchAbility::particle,
            FloatAbility.STREAM_CODEC.apply(ByteBufCodecs::optional), LaunchAbility::slowFall,
            LaunchAbility::new
    );

    public static LaunchAbility of(int doubleTapWindow, float height, Optional<FloatAbility> slowFall) {
        return new LaunchAbility(doubleTapWindow, height, Optional.empty(), Optional.empty(), slowFall);
    }

    /**
     * @return the vertical speed that carries a holder pulled down by {@code gravity} up to {@link #height()}
     */
    public double takeOffSpeed(double gravity) {
        double low = 0.0D;
        double high = Math.max(1.0D, this.height);
        while (apex(high, gravity) < this.height) {
            high *= 2.0D;
        }
        for (int i = 0; i < 40; i++) {
            double middle = (low + high) / 2.0D;
            if (apex(middle, gravity) < this.height) {
                low = middle;
            } else {
                high = middle;
            }
        }
        return high;
    }

    /**
     * @return how high a holder leaving at {@code speed} rises, moved then slowed tick after tick the way vanilla does
     */
    public static double apex(double speed, double gravity) {
        double height = 0.0D;
        double velocity = speed;
        for (int tick = 0; tick < MAX_TICKS && velocity > 0.0D; tick++) {
            height += velocity;
            velocity = (velocity - gravity) * AIR_DRAG;
        }
        return height;
    }
}
