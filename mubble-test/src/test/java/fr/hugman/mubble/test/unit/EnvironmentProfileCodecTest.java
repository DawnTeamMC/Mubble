package fr.hugman.mubble.test.unit;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import fr.hugman.mubble.world.voyage.environment.WeatherState;
import java.util.Optional;

import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Serialisation of {@link EnvironmentProfile}. Profiles are written by hand in data packs, so the
 * shape of this file is a contract with whoever is authoring one, and the network codec deciding to
 * carry a field it should not is a leak nobody would notice from in game.
 */
public class EnvironmentProfileCodecTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("a profile with every field filled survives a JSON round trip")
    void fullProfileRoundTrips() {
        CodecAssertions.assertJsonRoundTrip(EnvironmentProfile.DIRECT_CODEC, fullyPopulated());
    }

    @Test
    @DisplayName("a profile with every field left out survives a JSON round trip")
    void emptyProfileRoundTrips() {
        CodecAssertions.assertJsonRoundTrip(EnvironmentProfile.DIRECT_CODEC, EnvironmentProfile.EMPTY);
    }

    @Test
    @DisplayName("an empty object is a valid profile")
    void emptyObjectParses() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);

        var parsed = EnvironmentProfile.DIRECT_CODEC.parse(ops, JsonParser.parseString("{}"))
                .getOrThrow(error -> new AssertionError("{} should be a valid profile: " + error));

        assertEquals(EnvironmentProfile.EMPTY, parsed);
    }

    @Test
    @DisplayName("attribute ids may be written without the minecraft namespace")
    void unnamespacedAttributeIdsParse() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var json = JsonParser.parseString("""
                { "attributes": { "visual/sky_color": "#7b9fff", "minecraft:visual/fog_color": "#c0c0ff" } }
                """);

        var parsed = EnvironmentProfile.DIRECT_CODEC.parse(ops, json)
                .getOrThrow(error -> new AssertionError("could not parse: " + error));

        assertEquals(0xFF7B9FFF, parsed.attributes().applyModifier(EnvironmentAttributes.SKY_COLOR, 0));
        assertEquals(0xFFC0C0FF, parsed.attributes().applyModifier(EnvironmentAttributes.FOG_COLOR, 0));
    }

    @Test
    @DisplayName("a profile only touches the attributes it names")
    void unnamedAttributesFallThrough() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var json = JsonParser.parseString("""
                { "attributes": { "visual/sky_color": "#7b9fff" } }
                """);

        var parsed = EnvironmentProfile.DIRECT_CODEC.parse(ops, json)
                .getOrThrow(error -> new AssertionError("could not parse: " + error));

        assertTrue(parsed.attributes().contains(EnvironmentAttributes.SKY_COLOR));
        assertFalse(parsed.attributes().contains(EnvironmentAttributes.FOG_COLOR),
                "a profile that sets only sky_color must leave fog_color to the layer below");

        // Fall-through is per field: an attribute the profile does not name comes back untouched.
        assertEquals(4242, parsed.attributes().applyModifier(EnvironmentAttributes.FOG_COLOR, 4242));
    }

    @Test
    @DisplayName("the network codec drops the fields the client has no business knowing")
    void networkCodecDropsServerOnlyFields() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);

        var encoded = EnvironmentProfile.NETWORK_CODEC.encodeStart(ops, fullyPopulated())
                .getOrThrow(error -> new AssertionError("could not encode: " + error));
        var decoded = EnvironmentProfile.NETWORK_CODEC.parse(ops, encoded)
                .getOrThrow(error -> new AssertionError("could not decode: " + error));

        assertEquals(Optional.empty(), decoded.fixedTime(),
                "fixed_time is applied server-side; the client learns the time from the clock packets");
        assertEquals(Optional.empty(), decoded.weather(),
                "weather is server state; the client learns it from the weather packets");
        assertTrue(decoded.attributes().contains(EnvironmentAttributes.SKY_COLOR),
                "the attributes are the whole point of syncing a profile");
    }

    @Test
    @DisplayName("a malformed profile is rejected rather than silently ignored")
    void malformedProfileIsRejected() {
        CodecAssertions.assertRejects(EnvironmentProfile.DIRECT_CODEC,
                JsonParser.parseString("""
                        { "attributes": { "mubble:not_an_attribute": 1 } }
                        """));
        CodecAssertions.assertRejects(EnvironmentProfile.DIRECT_CODEC,
                JsonParser.parseString("""
                        { "weather": "drizzle" }
                        """));
        CodecAssertions.assertRejects(EnvironmentProfile.DIRECT_CODEC,
                JsonParser.parseString("""
                        { "attributes": { "visual/sky_color": "not a colour" } }
                        """));
    }

    @Test
    @DisplayName("the network codec can read what the file codec wrote")
    void networkCodecReadsFileCodecOutput() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);

        // Registry sync does not guarantee that the same codec is used on both ends: an entry loaded
        // through the file codec can be handed to the network codec. The two must therefore agree on
        // the *shape* of a profile, and differ only in which fields they carry.
        var written = EnvironmentProfile.DIRECT_CODEC.encodeStart(ops, fullyPopulated())
                .getOrThrow(error -> new AssertionError("could not encode: " + error));

        var read = EnvironmentProfile.NETWORK_CODEC.parse(ops, written);
        assertTrue(read.isSuccess(),
                () -> "the network codec could not read the file codec's output: " + read.error().map(Object::toString).orElse(""));
        assertTrue(read.getOrThrow().attributes().contains(EnvironmentAttributes.SKY_COLOR),
                "the attributes were lost in the crossover");
    }

    @Test
    @DisplayName("the file codec can read what the network codec wrote")
    void fileCodecReadsNetworkCodecOutput() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);

        var written = EnvironmentProfile.NETWORK_CODEC.encodeStart(ops, fullyPopulated())
                .getOrThrow(error -> new AssertionError("could not encode: " + error));

        var read = EnvironmentProfile.DIRECT_CODEC.parse(ops, written);
        assertTrue(read.isSuccess(),
                () -> "the file codec could not read the network codec's output: " + read.error().map(Object::toString).orElse(""));
        // Every field being optional means a shape mismatch decodes to an empty profile rather than
        // failing, so success alone proves nothing here.
        assertTrue(read.getOrThrow().attributes().contains(EnvironmentAttributes.SKY_COLOR),
                "the attributes were silently dropped in the crossover");
    }

    private static EnvironmentProfile fullyPopulated() {
        return new EnvironmentProfile(
                EnvironmentAttributeMap.builder()
                        .set(EnvironmentAttributes.SKY_COLOR, 0xFFFFA120)
                        .set(EnvironmentAttributes.FOG_COLOR, 0xFFFFB574)
                        .set(EnvironmentAttributes.SKY_LIGHT_LEVEL, 12.0F)
                        .build(),
                Optional.of(23000),
                Optional.of(WeatherState.CLEAR)
        );
    }
}
