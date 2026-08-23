package fr.hugman.mubble.world.voyage.trial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFileCodec;

/**
 * One trial, as written in a data pack.
 *
 * <pre>{@code
 * // data/<namespace>/trial/<id>.json
 * {
 *   "display_name": "Void Platform",
 *   "environment": "mubble-testmod:env_dawn",
 *   "platform": { "block": "minecraft:stone", "radius": 8, "spawn_y": 65 }
 * }
 * }</pre>
 *
 * <p><strong>For the POC a trial is a look and a floor.</strong> The two things it will obviously
 * also need — a ruleset (what the player may do) and an objective (what ends the trial) — are
 * deliberately absent rather than stubbed. An empty {@code Ruleset} registry would have to be
 * designed now, wrong, and then either migrated or lived with; a missing field can be added without
 * breaking a single existing file, because both would be optional anyway. The seam is this record:
 * they arrive here as two more components.
 *
 * @param displayName what the player is told they are entering
 * @param environment the look, resolved against the trial's node seed on entry
 * @param platform    the ground to build, since trial levels generate empty
 */
public record TrialDefinition(
        Component displayName,
        Holder<EnvironmentProfile> environment,
        TrialPlatform platform
) {
    public static final Codec<TrialDefinition> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("display_name").forGetter(TrialDefinition::displayName),
            EnvironmentProfile.CODEC.fieldOf("environment").forGetter(TrialDefinition::environment),
            TrialPlatform.CODEC.optionalFieldOf("platform", TrialPlatform.DEFAULT).forGetter(TrialDefinition::platform)
    ).apply(instance, TrialDefinition::new));

    /** For referencing a trial by id from a voyage. */
    public static final Codec<Holder<TrialDefinition>> CODEC =
            RegistryFileCodec.create(MubbleRegistries.TRIAL, DIRECT_CODEC);

    /**
     * {@return the clock time this trial's level should be parked at, if its environment names one}
     *
     * <p>Reached through the environment because that is where a data pack declares it, but it is
     * not an attribute and cannot be applied as a layer: clocks in 26.2 hang off the server, not the
     * level, so the only per-trial way to set one is at level creation. Whoever opens the level needs
     * this, and should not have to know what an environment profile is to get at it.
     */
    public Optional<Integer> fixedTime() {
        return this.environment.value().fixedTime();
    }
}
