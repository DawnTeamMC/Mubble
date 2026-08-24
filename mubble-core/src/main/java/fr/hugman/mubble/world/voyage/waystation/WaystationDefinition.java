package fr.hugman.mubble.world.voyage.waystation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.VoyageNodeContent;
import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import fr.hugman.mubble.world.voyage.trial.TrialPlatform;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFileCodec;

/**
 * A place between trials, as written in a data pack.
 *
 * <pre>{@code
 * // data/<namespace>/mubble/waystation/<id>.json
 * {
 *   "display_name": "Crossroads",
 *   "environment": "mubble-testmod:env_empty",
 *   "platform": { "block": "minecraft:polished_andesite", "radius": 5, "spawn_y": 65 }
 * }
 * }</pre>
 *
 * <p><strong>Bare on purpose.</strong> A waystation is a level you stand in and leave; it has no
 * shop, no upgrade, no reward, and completing one earns nothing. What it is for right now is being
 * somewhere a voyage can branch, which the graph handles rather than this record.
 *
 * <p>Identical to {@link fr.hugman.mubble.world.voyage.trial.TrialDefinition} today, and a separate
 * record anyway: a trial is where an objective and a ruleset go, a waystation is where a shop goes,
 * and neither has arrived. Sharing one record now would have to be undone the moment either does.
 */
public record WaystationDefinition(
        Component displayName,
        Holder<EnvironmentProfile> environment,
        TrialPlatform platform
) implements VoyageNodeContent {
    public static final Codec<WaystationDefinition> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("display_name").forGetter(WaystationDefinition::displayName),
            EnvironmentProfile.CODEC.fieldOf("environment").forGetter(WaystationDefinition::environment),
            TrialPlatform.CODEC.optionalFieldOf("platform", TrialPlatform.DEFAULT).forGetter(WaystationDefinition::platform)
    ).apply(instance, WaystationDefinition::new));

    /** For referencing a waystation by id from a voyage. */
    public static final Codec<Holder<WaystationDefinition>> CODEC =
            RegistryFileCodec.create(MubbleRegistries.WAYSTATION, DIRECT_CODEC, false);
}
