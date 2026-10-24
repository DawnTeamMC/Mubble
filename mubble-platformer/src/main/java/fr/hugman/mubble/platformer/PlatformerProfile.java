package fr.hugman.mubble.platformer;

import fr.hugman.mubble.platformer.registries.PlatformerBuiltInRegistries;
import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.MoveSettings;
import java.util.Map;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;

/**
 * A set of rules for the platformer movement layer: the physics, the grace mechanics, what the hands may
 * do, and every move it supports along with its numbers.
 * <p>
 * Profiles live in the {@code mubble:platformer_profile} dynamic registry and are synced to the clients
 * in the configuration phase. Their values are also reloaded by {@code /reload}, see
 * {@link PlatformerProfiles}.
 */
public record PlatformerProfile(
        PlatformerPhysics physics,
        PlatformerGrace grace,
        InteractionPolicy interaction,
        PlatformerCosts costs,
        PlatformerFallDamage fallDamage,
        PlatformerCameraHints camera,
        PlatformerValidation validation,
        Map<PlatformerMove, MoveSettings> moves
) {
    public static final Codec<Map<PlatformerMove, MoveSettings>> MOVES_CODEC = Codec.dispatchedMap(PlatformerBuiltInRegistries.PLATFORMER_MOVE.byNameCodec(), MoveSettings::codecFor);

    public static final Codec<PlatformerProfile> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlatformerPhysics.CODEC.fieldOf("physics").forGetter(PlatformerProfile::physics),
            PlatformerGrace.CODEC.fieldOf("grace").forGetter(PlatformerProfile::grace),
            InteractionPolicy.CODEC.fieldOf("interaction").forGetter(PlatformerProfile::interaction),
            PlatformerCosts.CODEC.fieldOf("costs").forGetter(PlatformerProfile::costs),
            PlatformerFallDamage.CODEC.fieldOf("fall_damage").forGetter(PlatformerProfile::fallDamage),
            PlatformerCameraHints.CODEC.optionalFieldOf("camera", PlatformerCameraHints.NEUTRAL).forGetter(PlatformerProfile::camera),
            PlatformerValidation.CODEC.optionalFieldOf("validation", PlatformerValidation.DEFAULT).forGetter(PlatformerProfile::validation),
            MOVES_CODEC.fieldOf("moves").forGetter(PlatformerProfile::moves)
    ).apply(instance, PlatformerProfile::new));

    public static final Codec<Holder<PlatformerProfile>> CODEC = RegistryFileCodec.create(PlatformerRegistries.PLATFORMER_PROFILE, DIRECT_CODEC);

    /** The whole profile, as sent after a {@code /reload}. Profiles are small enough not to need a hand-written codec. */
    public static final StreamCodec<RegistryFriendlyByteBuf, PlatformerProfile> DIRECT_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(DIRECT_CODEC);

    public Optional<MoveSettings> settings(PlatformerMove move) {
        return Optional.ofNullable(this.moves.get(move));
    }

    public boolean supports(PlatformerMove move) {
        return this.moves.containsKey(move);
    }

    /** The settings of {@code move}, or the defaults of its parameters when the profile does not list it. */
    public MoveSettings settingsOrDefault(PlatformerMove move) {
        var settings = this.moves.get(move);
        return settings == null ? MoveSettings.DEFAULT : settings;
    }
}
