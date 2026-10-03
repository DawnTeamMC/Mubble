package fr.hugman.mubble.world.arcade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import fr.hugman.mubble.world.arcade.move.MoveSettings;
import java.util.Map;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;

/**
 * A set of rules for the arcade movement layer: the physics, the grace mechanics, what the hands may
 * do, and every move it supports along with its numbers.
 * <p>
 * Profiles live in the {@code mubble:arcade_profile} dynamic registry and are synced to the clients
 * in the configuration phase. Their values are also reloaded by {@code /reload}, see
 * {@link ArcadeProfiles}.
 */
public record ArcadeProfile(
        ArcadePhysics physics,
        ArcadeGrace grace,
        InteractionPolicy interaction,
        ArcadeCosts costs,
        ArcadeFallDamage fallDamage,
        ArcadeCameraHints camera,
        ArcadeValidation validation,
        Map<ArcadeMove, MoveSettings> moves
) {
    public static final Codec<Map<ArcadeMove, MoveSettings>> MOVES_CODEC = Codec.dispatchedMap(MubbleBuiltInRegistries.ARCADE_MOVE.byNameCodec(), MoveSettings::codecFor);

    public static final Codec<ArcadeProfile> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ArcadePhysics.CODEC.fieldOf("physics").forGetter(ArcadeProfile::physics),
            ArcadeGrace.CODEC.fieldOf("grace").forGetter(ArcadeProfile::grace),
            InteractionPolicy.CODEC.fieldOf("interaction").forGetter(ArcadeProfile::interaction),
            ArcadeCosts.CODEC.fieldOf("costs").forGetter(ArcadeProfile::costs),
            ArcadeFallDamage.CODEC.fieldOf("fall_damage").forGetter(ArcadeProfile::fallDamage),
            ArcadeCameraHints.CODEC.optionalFieldOf("camera", ArcadeCameraHints.NEUTRAL).forGetter(ArcadeProfile::camera),
            ArcadeValidation.CODEC.optionalFieldOf("validation", ArcadeValidation.DEFAULT).forGetter(ArcadeProfile::validation),
            MOVES_CODEC.fieldOf("moves").forGetter(ArcadeProfile::moves)
    ).apply(instance, ArcadeProfile::new));

    public static final Codec<Holder<ArcadeProfile>> CODEC = RegistryFileCodec.create(MubbleRegistries.ARCADE_PROFILE, DIRECT_CODEC);

    /** The whole profile, as sent after a {@code /reload}. Profiles are small enough not to need a hand-written codec. */
    public static final StreamCodec<RegistryFriendlyByteBuf, ArcadeProfile> DIRECT_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(DIRECT_CODEC);

    public Optional<MoveSettings> settings(ArcadeMove move) {
        return Optional.ofNullable(this.moves.get(move));
    }

    public boolean supports(ArcadeMove move) {
        return this.moves.containsKey(move);
    }

    /** The settings of {@code move}, or the defaults of its parameters when the profile does not list it. */
    public MoveSettings settingsOrDefault(ArcadeMove move) {
        var settings = this.moves.get(move);
        return settings == null ? MoveSettings.DEFAULT : settings;
    }
}
