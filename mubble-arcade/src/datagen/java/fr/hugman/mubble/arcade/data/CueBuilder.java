package fr.hugman.mubble.arcade.data;

import fr.hugman.mubble.arcade.cue.Cue;
import fr.hugman.mubble.arcade.cue.CueShape;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

/**
 * Builds the cues of the shipped profiles, from vanilla sounds and particles only.
 */
public final class CueBuilder {
    private Optional<Holder<SoundEvent>> sound = Optional.empty();
    private float volume = 1.0F;
    private float pitch = 1.0F;
    private Optional<ParticleOptions> particle = Optional.empty();
    private boolean surface;
    private CueShape shape = CueShape.BURST;
    private int count;
    private float countPerSpeed;
    private float spread = 0.3F;
    private float speed = 0.05F;
    private int interval = 1;

    public static CueBuilder cue() {
        return new CueBuilder();
    }

    public CueBuilder sound(SoundEvent sound, float volume, float pitch) {
        this.sound = Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound));
        this.volume = volume;
        this.pitch = pitch;
        return this;
    }

    public CueBuilder sound(Holder<SoundEvent> sound, float volume, float pitch) {
        this.sound = Optional.of(sound);
        this.volume = volume;
        this.pitch = pitch;
        return this;
    }

    public CueBuilder particle(ParticleOptions particle, int count) {
        this.particle = Optional.of(particle);
        this.count = count;
        return this;
    }

    /** The breaking particles of the touched block. */
    public CueBuilder surface(int count) {
        this.surface = true;
        this.count = count;
        return this;
    }

    public CueBuilder shape(CueShape shape) {
        this.shape = shape;
        return this;
    }

    public CueBuilder perSpeed(float countPerSpeed) {
        this.countPerSpeed = countPerSpeed;
        return this;
    }

    public CueBuilder spread(float spread) {
        this.spread = spread;
        return this;
    }

    public CueBuilder speed(float speed) {
        this.speed = speed;
        return this;
    }

    public CueBuilder interval(int interval) {
        this.interval = interval;
        return this;
    }

    public Cue build() {
        return new Cue(this.sound, this.volume, this.pitch, this.particle, this.surface, this.shape, this.count, this.countPerSpeed, this.spread, this.speed, this.interval);
    }
}
