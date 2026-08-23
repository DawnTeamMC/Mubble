package fr.hugman.mubble.test.unit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.voyage.environment.EnvironmentAttributeChoices;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Candidate lists on environment attributes.
 *
 * <p>Two things are being defended here. One is the resolver: a voyage that does not reproduce from
 * its seed has no shareable run codes, and nothing in game would make that obvious. The other is the
 * codec, which is a hand-rebuilt copy of a private vanilla one — {@link #matchesVanillaForFixedValues}
 * is the test that notices when the original moves.
 */
public class EnvironmentAttributeChoicesTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("a fixed map is read exactly as vanilla reads it")
    void matchesVanillaForFixedValues() {
        // Every shape vanilla's entry codec supports: a plain value, a value that is not a colour,
        // an attribute the client never sees, and the long modifier form.
        JsonElement json = JsonParser.parseString("""
                {
                  "visual/sky_color": "#ffa120",
                  "visual/fog_start_distance": 12.5,
                  "gameplay/monsters_burn": true,
                  "visual/fog_color": { "modifier": "multiply", "argument": 0.5 }
                }
                """);
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);

        EnvironmentAttributeMap vanilla = EnvironmentAttributeMap.CODEC.parse(ops, json)
                .getOrThrow(error -> new AssertionError("vanilla could not parse: " + error));
        EnvironmentAttributeChoices ours = EnvironmentAttributeChoices.CODEC.parse(ops, json)
                .getOrThrow(error -> new AssertionError("we could not parse: " + error));

        assertEquals(vanilla, ours.resolve(0L),
                "the rebuilt entry codec no longer reads what vanilla reads");
        assertFalse(ours.isSeedDependent(), "nothing here names candidates");
    }

    @Test
    @DisplayName("a plain value and a one-entry list mean the same thing")
    void singletonListEqualsScalar() {
        assertEquals(resolve("""
                { "visual/sky_color": "#ffa120" }
                """, 0L), resolve("""
                { "visual/sky_color": ["#ffa120"] }
                """, 0L));
    }

    @Test
    @DisplayName("a candidate list survives a JSON round trip")
    void candidatesRoundTrip() {
        CodecAssertions.assertJsonRoundTrip(EnvironmentAttributeChoices.CODEC, parse("""
                { "visual/sky_color": ["#ff2244", "#3cb043"], "visual/fog_color": "#222222" }
                """));
    }

    @Test
    @DisplayName("an attribute that names no candidates at all is rejected")
    void emptyCandidateListIsRejected() {
        CodecAssertions.assertRejects(EnvironmentAttributeChoices.CODEC, JsonParser.parseString("""
                { "visual/sky_color": [] }
                """));
    }

    @Test
    @DisplayName("the same seed always resolves to the same value")
    void resolutionIsDeterministic() {
        EnvironmentAttributeChoices choices = parse("""
                { "visual/sky_color": ["#ff2244", "#3cb043", "#ffe000", "#7b2fff"] }
                """);

        assertEquals(sky(choices, 7L), sky(choices, 7L));
        assertEquals(sky(choices, 7L), sky(parse("""
                { "visual/sky_color": ["#ff2244", "#3cb043", "#ffe000", "#7b2fff"] }
                """), 7L), "two copies of the same profile resolved differently");
    }

    @Test
    @DisplayName("different seeds reach every candidate")
    void resolutionVariesWithTheSeed() {
        EnvironmentAttributeChoices choices = parse("""
                { "visual/sky_color": ["#ff2244", "#3cb043", "#ffe000", "#7b2fff"] }
                """);

        Set<Integer> seen = new HashSet<>();
        for (long seed = 0; seed < 200; seed++) {
            seen.add(sky(choices, seed));
        }
        assertEquals(4, seen.size(), "only " + seen.size() + " of the four candidates were ever drawn");
    }

    @Test
    @DisplayName("two lists of the same length do not move together")
    void attributesResolveIndependently() {
        // The failure this catches is a resolver that derives one seed for the whole profile: sky and
        // fog would then always take the same index, and a profile offering four of each would only
        // ever show four combinations instead of sixteen.
        EnvironmentAttributeChoices choices = parse("""
                {
                  "visual/sky_color": ["#000001", "#000002", "#000003", "#000004"],
                  "visual/fog_color": ["#000001", "#000002", "#000003", "#000004"]
                }
                """);

        Set<String> pairs = new HashSet<>();
        for (long seed = 0; seed < 200; seed++) {
            EnvironmentAttributeMap resolved = choices.resolve(seed);
            pairs.add(resolved.applyModifier(EnvironmentAttributes.SKY_COLOR, 0)
                    + "/" + resolved.applyModifier(EnvironmentAttributes.FOG_COLOR, 0));
        }
        assertEquals(16, pairs.size(), "sky and fog produced only " + pairs.size() + " of 16 combinations");
    }

    @Test
    @DisplayName("the fixed half and the resolved half are complementary")
    void fixedAndResolvedSplitCleanly() {
        EnvironmentAttributeChoices choices = parse("""
                { "visual/sky_color": ["#ff2244", "#3cb043"], "visual/fog_color": "#222222" }
                """);

        assertTrue(choices.isSeedDependent());
        assertTrue(choices.fixed().contains(EnvironmentAttributes.FOG_COLOR));
        assertFalse(choices.fixed().contains(EnvironmentAttributes.SKY_COLOR),
                "a seed-dependent attribute must not be in the half that is synced as data");

        assertTrue(choices.resolveCandidates(3L).contains(EnvironmentAttributes.SKY_COLOR));
        assertFalse(choices.resolveCandidates(3L).contains(EnvironmentAttributes.FOG_COLOR),
                "a fixed attribute must not be sent again as a resolved override");

        // Layering one on the other has to give the same thing as resolving the whole profile, or
        // the server and the client would be looking at different skies.
        EnvironmentAttributeMap whole = choices.resolve(3L);
        assertEquals(whole.applyModifier(EnvironmentAttributes.SKY_COLOR, 0),
                choices.resolveCandidates(3L).applyModifier(EnvironmentAttributes.SKY_COLOR, 0));
        assertEquals(whole.applyModifier(EnvironmentAttributes.FOG_COLOR, 0),
                choices.fixed().applyModifier(EnvironmentAttributes.FOG_COLOR, 0));
    }

    @Test
    @DisplayName("the network form carries no candidates and no server-only attributes")
    void networkCodecStripsCandidatesAndServerOnlyFields() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        EnvironmentAttributeChoices choices = parse("""
                {
                  "visual/sky_color": ["#ff2244", "#3cb043"],
                  "visual/fog_color": "#222222",
                  "gameplay/monsters_burn": true
                }
                """);

        var encoded = EnvironmentAttributeChoices.NETWORK_CODEC.encodeStart(ops, choices)
                .getOrThrow(error -> new AssertionError("could not encode: " + error));
        EnvironmentAttributeChoices decoded = EnvironmentAttributeChoices.NETWORK_CODEC.parse(ops, encoded)
                .getOrThrow(error -> new AssertionError("could not decode: " + error));

        assertFalse(decoded.isSeedDependent(), "a candidate list reached the client");
        assertFalse(decoded.fixed().contains(EnvironmentAttributes.SKY_COLOR),
                "the client was sent a value it must instead be told per trial");
        assertFalse(decoded.fixed().contains(EnvironmentAttributes.MONSTERS_BURN),
                "monsters_burn is not syncable, so it has no business on the wire");
        assertTrue(decoded.fixed().contains(EnvironmentAttributes.FOG_COLOR),
                "the fixed, syncable attributes are the whole point of syncing a profile");
    }

    @Test
    @DisplayName("the network codec can read a file that names candidates")
    void networkCodecReadsCandidates() {
        // Registry sync may hand the network codec something the file codec wrote. If that throws,
        // the client is disconnected on join by a profile it was never going to render anyway.
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var result = EnvironmentAttributeChoices.NETWORK_CODEC.parse(ops, JsonParser.parseString("""
                { "visual/sky_color": ["#ff2244", "#3cb043"], "visual/fog_color": "#222222" }
                """));

        assertTrue(result.isSuccess(),
                () -> "a candidate list broke the network codec: " + result.error().map(Object::toString).orElse(""));
        assertTrue(result.getOrThrow().fixed().contains(EnvironmentAttributes.FOG_COLOR));
    }

    @Test
    @DisplayName("candidates work on any attribute, not just colours")
    void candidatesAreNotSpecialCasedToColours() {
        EnvironmentAttributeChoices choices = parse("""
                { "gameplay/sky_light_level": [0.0, 15.0] }
                """);

        Set<Float> seen = new HashSet<>();
        for (long seed = 0; seed < 50; seed++) {
            seen.add(choices.resolve(seed).applyModifier(EnvironmentAttributes.SKY_LIGHT_LEVEL, -1.0F));
        }
        assertEquals(Set.of(0.0F, 15.0F), seen);
    }

    @Test
    @DisplayName("an unresolved profile is not silently treated as the first candidate")
    void candidatesAreNotLeftOutOfTheFixedHalf() {
        EnvironmentAttributeChoices choices = parse("""
                { "visual/sky_color": ["#ff2244", "#3cb043"] }
                """);

        assertEquals(EnvironmentAttributeMap.EMPTY, choices.fixed());
        assertNotEquals(EnvironmentAttributeMap.EMPTY, choices.resolveCandidates(0L));
    }

    private static EnvironmentAttributeChoices parse(String json) {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        return EnvironmentAttributeChoices.CODEC.parse(ops, JsonParser.parseString(json))
                .getOrThrow(error -> new AssertionError("could not parse " + json + ": " + error));
    }

    private static EnvironmentAttributeMap resolve(String json, long seed) {
        return parse(json).resolve(seed);
    }

    private static int sky(EnvironmentAttributeChoices choices, long seed) {
        return choices.resolve(seed).applyModifier(EnvironmentAttributes.SKY_COLOR, 0);
    }
}
