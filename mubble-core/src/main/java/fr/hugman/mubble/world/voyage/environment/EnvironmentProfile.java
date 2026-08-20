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
 * <p>{@link #attributes} is vanilla's own {@link EnvironmentAttributeMap}, so a profile speaks the
 * same language as a dimension type and gets per-field fall-through, modifier support
 * ({@code override}, {@code add}, {@code multiply}, …) and a syncable/non-syncable split for free.
 * Only the two engine-coupled fields vanilla does not express as attributes are our own.
 *
 * <pre>{@code
 * // data/<namespace>/environment_profile/<id>.json
 * {
 *   "attributes": {
 *     "sky_color": 16752640,
 *     "fog_color": 16744192,
 *     "sky_light_level": 15
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
        EnvironmentAttributeMap attributes,
        Optional<Integer> fixedTime,
        Optional<WeatherState> weather
) {
    /** The on-disk form. Everything is optional, so an empty file is a valid no-op profile. */
    public static final Codec<EnvironmentProfile> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EnvironmentAttributeMap.CODEC.optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(EnvironmentProfile::attributes),
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
     * <p>{@link EnvironmentAttributeMap#NETWORK_CODEC} additionally drops attributes vanilla marks
     * as not syncable, so purely server-side entries never leave the server.
     */
    public static final Codec<EnvironmentProfile> NETWORK_CODEC = EnvironmentAttributeMap.NETWORK_CODEC
            .xmap(attributes -> new EnvironmentProfile(attributes, Optional.empty(), Optional.empty()), EnvironmentProfile::attributes);

    /** For referencing a profile by id from another datapack file. */
    public static final Codec<Holder<EnvironmentProfile>> CODEC =
            RegistryFileCodec.create(MubbleRegistries.ENVIRONMENT_PROFILE, DIRECT_CODEC);

    public static final EnvironmentProfile EMPTY =
            new EnvironmentProfile(EnvironmentAttributeMap.EMPTY, Optional.empty(), Optional.empty());
}
