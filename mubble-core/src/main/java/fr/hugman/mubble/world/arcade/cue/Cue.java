package fr.hugman.mubble.world.arcade.cue;

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
        int interval
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
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("interval", 1).forGetter(Cue::interval)
    ).apply(instance, Cue::new));

    /** How many particles to spawn for a play at {@code speed} blocks per tick. */
    public int particleCount(double speed) {
        return this.count + (int) Math.floor(this.countPerSpeed * Math.max(0.0D, speed));
    }
}
