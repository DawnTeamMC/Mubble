package fr.hugman.mubble.arcade.cue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;

/**
 * A sound and a puff of particles a move plays at some point of its life, see {@link CueEvent}.
 * <p>
 * Everything is vanilla: the sound is a sound event, the particles are particle options. The only
 * twist is {@link #surface()}: instead of a fixed particle, the cue uses the breaking particles of
 * the block the player touches, which is how a landing on sand and one on stone tell apart.
 *
 * @param sound          the sound to play, if any
 * @param volume         volume of the sound
 * @param pitch          pitch of the sound
 * @param particle       the particle to spawn, ignored when {@code surface} is set
 * @param surface        whether to use the block particles of the touched surface instead
 * @param shape          how the particles are laid out
 * @param count          how many particles to spawn
 * @param countPerSpeed  how many more particles per block per tick of speed (landing speed for a
 *                       landing, horizontal speed otherwise)
 * @param spread         how far around the player the particles start, in blocks
 * @param speed          how fast the particles fly, in blocks per tick
 * @param interval       for {@link CueEvent#TICK} cues, how many ticks between two plays
 * @param rumble         how the controller of the player rumbles, if at all: only the player making
 *                       the move feels it, and only on a controller
 */
public record Cue(
        Optional<Holder<SoundEvent>> sound,
        float volume,
        float pitch,
        Optional<ParticleOptions> particle,
        boolean surface,
        CueShape shape,
        int count,
        float countPerSpeed,
        float spread,
        float speed,
        int interval,
        Optional<Rumble> rumble
) {
    public static final Codec<Cue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(Cue::sound),
            ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("volume", 1.0F).forGetter(Cue::volume),
            ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("pitch", 1.0F).forGetter(Cue::pitch),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(Cue::particle),
            Codec.BOOL.optionalFieldOf("surface", false).forGetter(Cue::surface),
            CueShape.CODEC.optionalFieldOf("shape", CueShape.BURST).forGetter(Cue::shape),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("count", 0).forGetter(Cue::count),
            Codec.floatRange(0.0F, 1000.0F).optionalFieldOf("count_per_speed", 0.0F).forGetter(Cue::countPerSpeed),
            Codec.floatRange(0.0F, 16.0F).optionalFieldOf("spread", 0.3F).forGetter(Cue::spread),
            Codec.floatRange(0.0F, 16.0F).optionalFieldOf("speed", 0.05F).forGetter(Cue::speed),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("interval", 1).forGetter(Cue::interval),
            Rumble.CODEC.optionalFieldOf("rumble").forGetter(Cue::rumble)
    ).apply(instance, Cue::new));

    /** How many particles to spawn for a play at {@code speed} blocks per tick. */
    public int particleCount(double speed) {
        return this.count + (int) Math.floor(this.countPerSpeed * Math.max(0.0D, speed));
    }

    /**
     * A short rumble of the controller.
     *
     * @param strong         strength of the low frequency motor, from 0 to 1
     * @param weak           strength of the high frequency motor, from 0 to 1
     * @param ticks          how long it lasts
     * @param scaleWithSpeed whether it is scaled down by the speed of the cue below one block per
     *                       tick, so that a soft landing barely rumbles
     */
    public record Rumble(float strong, float weak, int ticks, boolean scaleWithSpeed) {
        public static final Codec<Rumble> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.floatRange(0.0F, 1.0F).optionalFieldOf("strong", 0.0F).forGetter(Rumble::strong),
                Codec.floatRange(0.0F, 1.0F).optionalFieldOf("weak", 0.0F).forGetter(Rumble::weak),
                Codec.intRange(1, 100).optionalFieldOf("ticks", 2).forGetter(Rumble::ticks),
                Codec.BOOL.optionalFieldOf("scale_with_speed", false).forGetter(Rumble::scaleWithSpeed)
        ).apply(instance, Rumble::new));

        /** How much of the rumble a play at {@code speed} blocks per tick feels. */
        public float scale(double speed) {
            return this.scaleWithSpeed ? (float) Math.clamp(speed, 0.0D, 1.0D) : 1.0F;
        }
    }
}
