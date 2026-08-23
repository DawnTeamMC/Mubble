package fr.hugman.mubble.test.unit;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.voyage.trial.TrialPlatform;

import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The platform half of a trial definition.
 *
 * <p>Trial levels generate void, so a platform that fails to parse is a player falling forever. The
 * defaults matter as much as the parsing: a trial is allowed to leave the whole block out.
 */
public class TrialPlatformCodecTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("a fully specified platform survives a JSON round trip")
    void roundTrips() {
        CodecAssertions.assertJsonRoundTrip(TrialPlatform.CODEC, new TrialPlatform(Blocks.DEEPSLATE_TILES, 6, 80));
    }

    @Test
    @DisplayName("an empty object is a usable platform")
    void everyFieldHasADefault() {
        assertEquals(TrialPlatform.DEFAULT, parse("{}"));
    }

    @Test
    @DisplayName("the slab sits one block below where the player is put down")
    void spawnsOnTopOfTheSlab() {
        TrialPlatform platform = parse("{ \"spawn_y\": 100 }");

        assertEquals(100.0D, platform.spawnPos(0, 0).y());
        assertEquals(0.5D, platform.spawnPos(0, 0).x(), "the player should land on the middle of the slab");
    }

    @Test
    @DisplayName("a platform that could not be built is refused")
    void impossiblePlatformsAreRejected() {
        // Below the bottom of the world: the slab goes at spawn_y - 1, so -64 has nowhere to sit.
        CodecAssertions.assertRejects(TrialPlatform.CODEC, JsonParser.parseString("{ \"spawn_y\": -64 }"));
        CodecAssertions.assertRejects(TrialPlatform.CODEC, JsonParser.parseString("{ \"spawn_y\": 1000 }"));
        CodecAssertions.assertRejects(TrialPlatform.CODEC, JsonParser.parseString("{ \"radius\": -1 }"));
        CodecAssertions.assertRejects(TrialPlatform.CODEC, JsonParser.parseString("{ \"block\": \"minecraft:not_a_block\" }"));
    }

    private static TrialPlatform parse(String json) {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        return TrialPlatform.CODEC.parse(ops, JsonParser.parseString(json))
                .getOrThrow(error -> new AssertionError("could not parse " + json + ": " + error));
    }
}
