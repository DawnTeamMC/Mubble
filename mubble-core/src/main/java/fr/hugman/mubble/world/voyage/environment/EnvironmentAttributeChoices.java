package fr.hugman.mubble.world.voyage.environment;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.voyage.VoyageSeeds;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.AttributeModifier;

/**
 * An {@link EnvironmentAttributeMap} in which any attribute may name several candidate values
 * instead of one, to be chosen by seed.
 *
 * <pre>{@code
 * {
 *   "visual/fog_color": "#222222",                         // fixed
 *   "visual/sky_color": ["#ff2244", "#3cb043", "#ffe000"]  // chosen by the node seed
 * }
 * }</pre>
 *
 * <p>This is a general capability rather than a feature of one attribute: anything expressible as an
 * attribute can be given as a list, gameplay booleans included. A plain value and a one-entry list
 * mean exactly the same thing, and an empty list is an authoring error rather than a value.
 *
 * <p>Choices are resolved <strong>server-side, once, on trial entry</strong>. The client is sent the
 * value that was picked, never the list and never the seed, so it cannot disagree about the outcome
 * and cannot be read to predict a future one.
 *
 * <h2>Why this is not vanilla's codec</h2>
 *
 * <p>{@link EnvironmentAttributeMap#CODEC} dispatches each attribute to a per-attribute entry codec
 * that has no list form, and that entry codec is private. So it is rebuilt here, in the same shape,
 * out of the public parts: {@link EnvironmentAttribute#valueCodec()}, the attribute type's modifier
 * codec, and {@link AttributeModifier#argumentCodec}. The tests feed the same JSON to both codecs
 * and compare, so a change to that shape shows up as a failing test rather than as data packs
 * quietly changing meaning.
 */
public final class EnvironmentAttributeChoices {
    public static final EnvironmentAttributeChoices EMPTY = new EnvironmentAttributeChoices(Map.of());

    /** The on-disk form: each attribute maps to either one value or a list of candidates. */
    public static final Codec<EnvironmentAttributeChoices> CODEC = Codec.lazyInitialized(
            () -> Codec.dispatchedMap(EnvironmentAttributes.CODEC, Util.memoize(EnvironmentAttributeChoices::optionsCodec))
                    .xmap(EnvironmentAttributeChoices::new, choices -> choices.options)
    );

    /**
     * The over-the-wire form: the fixed attributes, and only the syncable ones.
     *
     * <p>Candidates are dropped rather than sent, because the client must never see a list. The
     * value actually picked for a trial reaches it as a resolved override inside
     * {@code ActiveEnvironmentPayload} instead.
     *
     * <p>It still <em>reads</em> the full form. Registry sync does not promise that both ends use
     * the same codec, so this has to survive being handed whatever a data pack wrote — see the shape
     * note on {@link EnvironmentProfile#NETWORK_CODEC}.
     */
    public static final Codec<EnvironmentAttributeChoices> NETWORK_CODEC =
            CODEC.xmap(EnvironmentAttributeChoices::syncableFixed, EnvironmentAttributeChoices::syncableFixed);

    private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeMap.Entry<?, ?>>> options;

    private EnvironmentAttributeChoices(Map<EnvironmentAttribute<?>, List<EnvironmentAttributeMap.Entry<?, ?>>> options) {
        this.options = options;
    }

    /** Wraps a plain attribute map, in which nothing is seed-dependent. */
    public static EnvironmentAttributeChoices of(EnvironmentAttributeMap map) {
        Map<EnvironmentAttribute<?>, List<EnvironmentAttributeMap.Entry<?, ?>>> options = new HashMap<>();
        for (EnvironmentAttribute<?> attribute : map.keySet()) {
            options.put(attribute, List.of(map.get(attribute)));
        }
        return options.isEmpty() ? EMPTY : new EnvironmentAttributeChoices(Map.copyOf(options));
    }

    /** {@return whether anything here depends on the seed} */
    public boolean isSeedDependent() {
        return this.options.values().stream().anyMatch(candidates -> candidates.size() > 1);
    }

    /** {@return the attributes naming a single value} — the half that is safe to sync as data. */
    public EnvironmentAttributeMap fixed() {
        return this.collect(candidates -> candidates.size() == 1, 0L);
    }

    /**
     * {@return the attributes naming several candidates, each resolved against {@code nodeSeed}}
     *
     * <p>The complement of {@link #fixed()}: layering the two gives the whole profile. They are kept
     * apart because the fixed half travels to the client inside the profile registry, while this
     * half has to be sent per trial as an already-resolved override.
     */
    public EnvironmentAttributeMap resolveCandidates(long nodeSeed) {
        return this.collect(candidates -> candidates.size() > 1, nodeSeed);
    }

    /** {@return the whole thing, fixed and resolved, as one map} */
    public EnvironmentAttributeMap resolve(long nodeSeed) {
        return this.collect(candidates -> true, nodeSeed);
    }

    private EnvironmentAttributeMap collect(Predicate<List<EnvironmentAttributeMap.Entry<?, ?>>> keep, long nodeSeed) {
        EnvironmentAttributeMap.Builder builder = EnvironmentAttributeMap.builder();
        this.options.forEach((attribute, candidates) -> {
            if (keep.test(candidates)) {
                put(builder, attribute, choose(attribute, candidates, nodeSeed));
            }
        });
        return builder.build();
    }

    private static EnvironmentAttributeMap.Entry<?, ?> choose(
            EnvironmentAttribute<?> attribute, List<EnvironmentAttributeMap.Entry<?, ?>> candidates, long nodeSeed) {
        if (candidates.size() == 1) {
            return candidates.getFirst();
        }
        // Salted with the attribute id, so two attributes offering the same number of candidates vary
        // independently instead of always landing on the same index together.
        long seed = VoyageSeeds.derive(nodeSeed, BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.getKey(attribute).toString());
        return candidates.get(VoyageSeeds.pick(seed, candidates.size()));
    }

    private EnvironmentAttributeChoices syncableFixed() {
        Map<EnvironmentAttribute<?>, List<EnvironmentAttributeMap.Entry<?, ?>>> kept = new HashMap<>();
        this.options.forEach((attribute, candidates) -> {
            if (candidates.size() == 1 && attribute.isSyncable()) {
                kept.put(attribute, candidates);
            }
        });
        return kept.isEmpty() ? EMPTY : new EnvironmentAttributeChoices(Map.copyOf(kept));
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof EnvironmentAttributeChoices other && this.options.equals(other.options);
    }

    @Override
    public int hashCode() {
        return this.options.hashCode();
    }

    @Override
    public String toString() {
        return this.options.toString();
    }

    /**
     * Puts an entry whose value type has been erased back into a builder.
     *
     * <p>The map is heterogeneous — every attribute has its own value type — so holding one requires
     * wildcards, and putting one back requires telling the compiler what the codec already proved
     * when it parsed that entry against that exact attribute.
     */
    @SuppressWarnings("unchecked")
    private static void put(EnvironmentAttributeMap.Builder builder, EnvironmentAttribute<?> attribute, EnvironmentAttributeMap.Entry<?, ?> entry) {
        builder.modify(
                (EnvironmentAttribute<Object>) attribute,
                (AttributeModifier<Object, Object>) entry.modifier(),
                entry.argument());
    }

    /** {@return the codec for one attribute's value: either one entry, or a non-empty list of them} */
    private static Codec<List<EnvironmentAttributeMap.Entry<?, ?>>> optionsCodec(EnvironmentAttribute<?> attribute) {
        Codec<EnvironmentAttributeMap.Entry<?, ?>> single = erasedEntryCodec(attribute);
        Codec<List<EnvironmentAttributeMap.Entry<?, ?>>> list = single.listOf().validate(
                candidates -> candidates.isEmpty()
                        ? DataResult.error(() -> "An attribute must name at least one candidate")
                        : DataResult.success(candidates));

        return Codec.either(single, list).xmap(
                either -> either.map(List::of, Function.identity()),
                // A one-entry list is written back as a plain value: the two mean the same thing, and
                // the plain form is the one a human would have written.
                candidates -> candidates.size() == 1 ? Either.left(candidates.getFirst()) : Either.right(candidates));
    }

    @SuppressWarnings("unchecked")
    private static Codec<EnvironmentAttributeMap.Entry<?, ?>> erasedEntryCodec(EnvironmentAttribute<?> attribute) {
        // Built against the attribute's own value type, then held with that type erased, because the
        // map it goes into holds a different value type per key.
        return (Codec<EnvironmentAttributeMap.Entry<?, ?>>) (Codec<?>) entryCodec(attribute);
    }

    /** A rebuild of the private {@code EnvironmentAttributeMap.Entry#createCodec}. Keep it identical. */
    private static <Value> Codec<EnvironmentAttributeMap.Entry<Value, ?>> entryCodec(EnvironmentAttribute<Value> attribute) {
        Function<AttributeModifier<Value, ?>, MapCodec<? extends EnvironmentAttributeMap.Entry<Value, ?>>> byModifier =
                Util.memoize(modifier -> modifiedEntryCodec(attribute, modifier));

        Codec<EnvironmentAttributeMap.Entry<Value, ?>> modified = attribute.type().modifierCodec()
                .dispatch("modifier", EnvironmentAttributeMap.Entry::modifier, byModifier);

        return Codec.either(attribute.valueCodec(), modified).xmap(
                either -> either.map(value -> new EnvironmentAttributeMap.Entry<>(value, AttributeModifier.override()), entry -> entry),
                EnvironmentAttributeChoices::asOverrideOrEntry);
    }

    private static <Value, Argument> MapCodec<EnvironmentAttributeMap.Entry<Value, Argument>> modifiedEntryCodec(
            EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                modifier.argumentCodec(attribute).fieldOf("argument").forGetter(EnvironmentAttributeMap.Entry::argument)
        ).apply(instance, argument -> new EnvironmentAttributeMap.Entry<>(argument, modifier)));
    }

    @SuppressWarnings("unchecked")
    private static <Value> Either<Value, EnvironmentAttributeMap.Entry<Value, ?>> asOverrideOrEntry(EnvironmentAttributeMap.Entry<Value, ?> entry) {
        // An override's argument is its value, which is what makes the short form possible at all.
        return entry.modifier() == AttributeModifier.override()
                ? Either.left((Value) entry.argument())
                : Either.right(entry);
    }
}
