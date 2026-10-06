package fr.hugman.mubble.world.power_up;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public record PowerUpCosmectics(
        Optional<ParticleOptions> particle,
        Optional<Holder<SoundEvent>> obtainSound,
        Optional<Holder<SoundEvent>> emitSound,
        Optional<Holder<SoundEvent>> looseSound,
        Optional<Holder<SoundEvent>> refillSound,
        Optional<Identifier> humanoidOverlayAssetId,
        boolean emissiveOverlay
) {
    public static final PowerUpCosmectics EMPTY = new PowerUpCosmectics(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), false);

    public static final Codec<PowerUpCosmectics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(PowerUpCosmectics::particle),
            SoundEvent.CODEC.optionalFieldOf("obtain_sound").forGetter(PowerUpCosmectics::obtainSound),
            SoundEvent.CODEC.optionalFieldOf("emit_sound").forGetter(PowerUpCosmectics::emitSound),
            SoundEvent.CODEC.optionalFieldOf("loose_sound").forGetter(PowerUpCosmectics::looseSound),
            SoundEvent.CODEC.optionalFieldOf("refill_sound").forGetter(PowerUpCosmectics::refillSound),
            Identifier.CODEC.optionalFieldOf("humanoid_overlay_asset_id").forGetter(PowerUpCosmectics::humanoidOverlayAssetId),
            Codec.BOOL.optionalFieldOf("emissive_overlay", false).forGetter(PowerUpCosmectics::emissiveOverlay)
    ).apply(instance, PowerUpCosmectics::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpCosmectics> STREAM_CODEC = StreamCodec.composite(
            ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpCosmectics::particle,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpCosmectics::obtainSound,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpCosmectics::emitSound,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpCosmectics::looseSound,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpCosmectics::refillSound,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), PowerUpCosmectics::humanoidOverlayAssetId,
            ByteBufCodecs.BOOL, PowerUpCosmectics::emissiveOverlay,
            PowerUpCosmectics::new
    );
}
