package fr.hugman.mubble.sounds;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.level.Level;

public final class SoundConfig {
    public static final Codec<SoundConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.CODEC.fieldOf("sound").forGetter(o -> o.sound),
            FloatProviders.CODEC.fieldOf("volume").forGetter(o -> o.volume),
            FloatProviders.CODEC.fieldOf("pitch").forGetter(o -> o.pitch)
    ).apply(instance, SoundConfig::new));
    private Holder<SoundEvent> sound;
    private FloatProvider volume;
    private FloatProvider pitch;

    public SoundConfig(Holder<SoundEvent> sound, FloatProvider volume, FloatProvider pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundConfig(SoundEvent sound, FloatProvider volume, FloatProvider pitch) {
        this(Holder.direct(sound), volume, pitch);
    }

    public SoundConfig(Holder<SoundEvent> sound, float volume, float pitch) {
        this(sound, ConstantFloat.of(volume), ConstantFloat.of(pitch));
    }

    public SoundConfig(SoundEvent sound, float volume, float pitch) {
        this(Holder.direct(sound), ConstantFloat.of(volume), ConstantFloat.of(pitch));
    }

    public Holder<SoundEvent> sound() {
        return sound;
    }

    public void sound(Holder<SoundEvent> sound) {
        this.sound = sound;
    }

    public FloatProvider volume() {
        return volume;
    }

    public void volume(FloatProvider volume) {
        this.volume = volume;
    }

    public FloatProvider pitch() {
        return pitch;
    }

    public void pitch(FloatProvider pitch) {
        this.pitch = pitch;
    }

    public void play(RandomSource random, Level level, final double x, final double y, final double z, final SoundSource source) {
        level.playSound(null, x, y, z, this.sound, source, this.volume.sample(random), this.pitch.sample(random));
    }
}
