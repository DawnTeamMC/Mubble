package fr.hugman.mubble.world.voyage.environment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.attribute.EnvironmentAttributeMap;

/**
 * A named, reusable set of environment overrides.
 *
 * <p>A profile is a layer in the environment attribute stack, not an owner of anything. Referencing
 * one by id is what lets a trial look completely different without needing its own dimension or its
 * own biome, and lets the same look back a voyage trial, a story Arena and a constellation world.
 *
 * <p>{@link #attributes} speaks vanilla's {@link EnvironmentAttributeMap} vocabulary, so a profile
 * says the same things a dimension type can and gets per-field fall-through, modifier support
 * ({@code override}, {@code add}, {@code multiply}, …) and a syncable/non-syncable split for free.
 * On top of that, any attribute may name a list of candidates chosen by the trial's node seed — see
 * {@link EnvironmentAttributeChoices}. Only the two engine-coupled fields vanilla does not express
 * as attributes are our own.
 *
 * <pre>{@code
 * // data/<namespace>/environment_profile/<id>.json
 * {
 *   "attributes": {
 *     "visual/sky_color": "#ffa120",
 *     "visual/fog_color": ["#ffb574", "#b57420"],
 *     "gameplay/sky_light_level": 12.0
 *   },
 *   "fixed_time": 18000,
 *   "weather": "clear"
 * }
 * }</pre>
 *
 * @param attributes the attribute layer this profile contributes; every entry optional
 * @param fixedTime  locks the level's day-time clock, in ticks
 * @param weather    forces the level's weather
 */
public record EnvironmentProfile(
        EnvironmentAttributeChoices attributes,
        Optional<Integer> fixedTime,
        Optional<WeatherState> weather
) {
    /** The on-disk form. Everything is optional, so an empty file is a valid no-op profile. */
    public static final Codec<EnvironmentProfile> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EnvironmentAttributeChoices.CODEC.optionalFieldOf("attributes", EnvironmentAttributeChoices.EMPTY).forGetter(EnvironmentProfile::attributes),
            Codec.INT.optionalFieldOf("fixed_time").forGetter(EnvironmentProfile::fixedTime),
            WeatherState.CODEC.optionalFieldOf("weather").forGetter(EnvironmentProfile::weather)
    ).apply(instance, EnvironmentProfile::new));

    /**
     * The over-the-wire form.
     *
     * <p>Deliberately narrower than {@link #DIRECT_CODEC}: the client renders, so it gets the
     * attributes and nothing else. {@code fixed_time} and {@code weather} are applied to the level
     * server-side and reach the client through the vanilla clock and weather packets it already
     * understands — sending them again would invite the client to recompute what it was told.
     *
     * <p>{@link EnvironmentAttributeChoices#NETWORK_CODEC} additionally drops attributes vanilla
     * marks as not syncable, and every seed-dependent candidate list. The value a list resolved to
     * arrives per trial in {@code ActiveEnvironmentPayload} instead, so the client is told what its
     * sky is rather than how it was decided.
     *
     * <p><strong>Same shape as {@link #DIRECT_CODEC}, on purpose.</strong> Registry sync does not
     * promise that both ends use the same codec — an entry read from disk with the file codec can be
     * handed to the network codec. So the two differ only in which fields they carry, never in the
     * structure around them. Making this a bare attribute map instead reads the whole profile object
     * as a map of attribute ids and fails with "Unknown registry key ... minecraft:attributes",
     * or worse, silently decodes to an empty profile in the other direction. There are tests for
     * both crossings.
     */
    public static final Codec<EnvironmentProfile> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EnvironmentAttributeChoices.NETWORK_CODEC.optionalFieldOf("attributes", EnvironmentAttributeChoices.EMPTY).forGetter(EnvironmentProfile::attributes)
    ).apply(instance, attributes -> new EnvironmentProfile(attributes, Optional.empty(), Optional.empty())));

    /**
     * For referencing a profile by id from another datapack file.
     *
     * <p>By id only — inlining a profile into the file that uses it is refused. The client is told
     * which environment to apply by naming the profile, so a profile with no name could be applied
     * on the server and never rendered. Rejecting it while the data pack loads beats discovering it
     * as a sky that only changes in single player.
     */
    public static final Codec<Holder<EnvironmentProfile>> CODEC =
            RegistryFileCodec.create(MubbleRegistries.ENVIRONMENT_PROFILE, DIRECT_CODEC, false);

    public static final EnvironmentProfile EMPTY =
            new EnvironmentProfile(EnvironmentAttributeChoices.EMPTY, Optional.empty(), Optional.empty());
}
