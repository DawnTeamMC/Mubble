package fr.hugman.mubble.test.unit.arcade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.world.arcade.animation.ArcadeAnimationData;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The animations the client ships, read the way the client reads them: a broken file would only show
 * as a player frozen mid-move, so they are checked here instead.
 */
public class ArcadeAnimationDataTest {
    /** The moves vanilla animates well enough on its own. */
    private static final Set<String> VANILLA_ANIMATED = Set.of("walk", "run", "fall", "crouch");

    private static Map<String, ArcadeAnimationData> shipped;

    @BeforeAll
    static void readShippedAnimations() throws IOException {
        Registrations.registerEverything();
        var dir = Path.of(System.getProperty("mubble.core.dir"), "src/client/resources/assets/mubble/animations/arcade");
        shipped = new HashMap<>();
        try (Stream<Path> files = Files.list(dir)) {
            for (var file : files.filter(f -> f.toString().endsWith(".json")).toList()) {
                var name = file.getFileName().toString().replace(".json", "");
                try {
                    shipped.put(name, ArcadeAnimationData.parse(JsonParser.parseString(Files.readString(file)).getAsJsonObject()));
                } catch (JsonParseException | IllegalStateException e) {
                    throw new AssertionError("The animation " + name + " does not read: " + e.getMessage(), e);
                }
            }
        }
    }

    @Test
    @DisplayName("every move vanilla does not animate has an animation")
    void everyMoveIsAnimated() {
        var missing = new TreeSet<String>();
        for (var move : MubbleBuiltInRegistries.ARCADE_MOVE) {
            var name = move.id().getPath();
            if (!VANILLA_ANIMATED.contains(name) && !shipped.containsKey(name)) {
                missing.add(name);
            }
        }
        assertTrue(missing.isEmpty(), "moves without an animation: " + missing);
        var unknown = new TreeSet<>(shipped.keySet());
        MubbleBuiltInRegistries.ARCADE_MOVE.forEach(move -> unknown.remove(move.id().getPath()));
        assertTrue(unknown.isEmpty(), "animations of no move: " + unknown);
    }

    @Test
    @DisplayName("flips turn the whole body all the way around, each its own way")
    void flipsTurnAllTheWayAround() {
        assertEquals(360.0F, finalBodyRotation("backflip")[0], 1.0E-3F, "a back flip turns over backwards");
        assertEquals(-360.0F, finalBodyRotation("triple_jump")[0], 1.0E-3F, "a triple jump turns over forwards");
        assertEquals(360.0F, Math.abs(finalBodyRotation("side_somersault")[2]), 1.0E-3F, "a side somersault turns over sideways");
    }

    private static float[] finalBodyRotation(String name) {
        var animation = shipped.get(name);
        var frames = animation.body().orElseThrow(() -> new AssertionError(name + " has no body channel")).get(ArcadeAnimationData.Target.ROTATION);
        var last = frames.getLast().post();
        return new float[]{last.x, last.y, last.z};
    }

    @Test
    @DisplayName("an animation naming a bone the player does not have is refused")
    void unknownBonesAreRefused() {
        var json = parse("{\"length\": 1, \"bones\": {\"tail\": {\"rotation\": [0, 0, 0]}}}");
        assertThrows(JsonParseException.class, () -> ArcadeAnimationData.parse(json));
    }

    @Test
    @DisplayName("a keyframe past the end of the animation is refused")
    void keyframesStayWithinTheLength() {
        var json = parse("{\"length\": 0.5, \"bones\": {\"head\": {\"rotation\": {\"0.0\": [0, 0, 0], \"0.8\": [10, 0, 0]}}}}");
        assertThrows(JsonParseException.class, () -> ArcadeAnimationData.parse(json));
    }

    @Test
    @DisplayName("keyframes read as a list, as an object keyed by time, or as one value held")
    void keyframeShapes() {
        var json = parse("""
                {"length": 1, "axes": "bedrock", "bones": {
                  "head": {"rotation": [{"time": 0.5, "value": [1, 2, 3]}, {"time": 0, "value": [0, 0, 0]}]},
                  "body": {"position": {"1.0": {"pre": [0, 1, 0], "post": [0, 2, 0], "lerp_mode": "catmullrom"}}},
                  "right_arm": {"scale": [1, 2, 1]}
                }}""");
        var data = ArcadeAnimationData.parse(json);
        assertTrue(data.bedrock());
        var head = data.bones().get("head").get(ArcadeAnimationData.Target.ROTATION);
        assertEquals(List.of(0.0F, 0.5F), head.stream().map(ArcadeAnimationData.Frame::time).toList(), "keyframes are sorted by time");
        var body = data.bones().get("body").get(ArcadeAnimationData.Target.POSITION).getFirst();
        assertTrue(body.smooth());
        assertEquals(1.0F, body.pre().y);
        assertEquals(2.0F, body.post().y);
        assertEquals(2.0F, data.bones().get("right_arm").get(ArcadeAnimationData.Target.SCALE).getFirst().post().y);
        assertFalse(data.loop());
    }

    private static JsonObject parse(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
}
