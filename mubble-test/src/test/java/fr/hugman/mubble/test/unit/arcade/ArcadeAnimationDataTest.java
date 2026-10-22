package fr.hugman.mubble.test.unit.arcade;

import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.arcade.animation.ArcadeAnimationData;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import net.minecraft.util.EasingType;
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
        var dir = Path.of(System.getProperty("mubble.arcade.dir"), "src/client/resources/assets/mubble/animations/arcade");
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
        for (var move : ArcadeBuiltInRegistries.ARCADE_MOVE) {
            var name = move.id().getPath();
            if (!VANILLA_ANIMATED.contains(name) && !shipped.containsKey(name)) {
                missing.add(name);
            }
        }
        assertTrue(missing.isEmpty(), "moves without an animation: " + missing);
        var unknown = new TreeSet<>(shipped.keySet());
        ArcadeBuiltInRegistries.ARCADE_MOVE.forEach(move -> unknown.remove(move.id().getPath()));
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

    @Test
    @DisplayName("keyframes ease the way to them with vanilla's easing types, by name or as a cubic Bezier")
    void keyframesEase() {
        var json = parse("""
                {"length": 1, "blend_in": 0.3, "bones": {"head": {"rotation": {
                  "0.0": [0, 0, 0],
                  "0.5": {"post": [10, 0, 0], "easing": "out_back"},
                  "1.0": {"post": [20, 0, 0], "easing": {"cubic_bezier": [0.4, 0, 0.6, 1]}}
                }}}}""");
        var data = ArcadeAnimationData.parse(json);
        assertEquals(0.3F, data.blendIn());
        var frames = data.bones().get("head").get(ArcadeAnimationData.Target.ROTATION);
        assertEquals(EasingType.LINEAR, frames.get(0).easing(), "no easing is a straight line");
        assertEquals(EasingType.OUT_BACK, frames.get(1).easing());
        var bezier = frames.get(2).easing();
        assertEquals(0.0F, bezier.apply(0.0F), 1.0E-4F);
        assertEquals(0.5F, bezier.apply(0.5F), 1.0E-3F, "a symmetric curve goes through the middle");
        assertEquals(1.0F, bezier.apply(1.0F), 1.0E-4F);
        assertEquals(ArcadeAnimationData.DEFAULT_BLEND_IN, ArcadeAnimationData.parse(parse("{\"length\": 1}")).blendIn());

        var unknown = parse("{\"length\": 1, \"bones\": {\"head\": {\"rotation\": {\"1.0\": {\"post\": [0, 0, 0], \"easing\": \"wobbly\"}}}}}");
        assertThrows(JsonParseException.class, () -> ArcadeAnimationData.parse(unknown));
    }

    @Test
    @DisplayName("impacts squash the whole body and spring back to its shape")
    void impactsSquashAndStretch() {
        for (var name : List.of("ground_pound_land", "land")) {
            var scale = shipped.get(name).body().orElseThrow().get(ArcadeAnimationData.Target.SCALE);
            var first = scale.getFirst().post();
            assertTrue(first.y < 1.0F && first.x > 1.0F, name + " starts squashed, got " + first);
            var last = scale.getLast().post();
            assertEquals(1.0F, last.y, 1.0E-6F, name + " ends in shape");
        }
        var drop = shipped.get("ground_pound").body().orElseThrow().get(ArcadeAnimationData.Target.SCALE).getLast().post();
        assertTrue(drop.y > 1.0F && drop.x < 1.0F, "a ground pound drops stretched, got " + drop);
    }

    @Test
    @DisplayName("a side somersault plays side on, the side looking at the camera; everything else faces forward")
    void sideSomersaultFacesTheCamera() {
        assertEquals(ArcadeAnimationData.Facing.SIDE_TO_CAMERA, shipped.get("side_somersault").face());
        assertEquals(ArcadeAnimationData.Facing.FORWARD, shipped.get("backflip").face());
        assertEquals(ArcadeAnimationData.Facing.FORWARD, ArcadeAnimationData.parse(parse("{\"length\": 1}")).face());
        assertThrows(JsonParseException.class, () -> ArcadeAnimationData.parse(parse("{\"length\": 1, \"face\": \"backwards\"}")));
    }

    @Test
    @DisplayName("a mirrored animation swaps its sides, and turns and moves sideways the other way")
    void mirroring() {
        var data = ArcadeAnimationData.parse(parse("""
                {"length": 1, "face": "side_to_camera", "bones": {
                  "right_arm": {"rotation": {"0.0": [0, 0, 0], "1.0": {"post": [-20, 10, 90], "easing": "out_back"}}},
                  "left_leg": {"position": [1, 2, 3]},
                  "head": {"rotation": [5, 6, 7], "scale": [1, 2, 3]}
                }, "body": {"pivot": [0.25, 0.9, 0], "rotation": {"0.0": [0, 0, 0], "1.0": [30, 0, 360]}}}""")).mirrored();
        var arm = data.bones().get("left_arm").get(ArcadeAnimationData.Target.ROTATION).getLast();
        assertEquals(-20.0F, arm.post().x, "a limb swinging forward still swings forward");
        assertEquals(-10.0F, arm.post().y);
        assertEquals(-90.0F, arm.post().z, "a right arm raised to its side is a left arm raised to the other side");
        assertEquals(EasingType.OUT_BACK, arm.easing(), "the timing stays");
        assertFalse(data.bones().containsKey("right_arm"));
        assertEquals(-1.0F, data.bones().get("right_leg").get(ArcadeAnimationData.Target.POSITION).getFirst().post().x);
        var head = data.bones().get("head");
        assertEquals(-6.0F, head.get(ArcadeAnimationData.Target.ROTATION).getFirst().post().y);
        assertEquals(2.0F, head.get(ArcadeAnimationData.Target.SCALE).getFirst().post().y, "scales stay");
        var body = data.body().orElseThrow().get(ArcadeAnimationData.Target.ROTATION).getLast().post();
        assertEquals(30.0F, body.x);
        assertEquals(-360.0F, body.z, "a cartwheel to the left becomes one to the right");
        assertEquals(-0.25F, data.pivot().x);
        assertEquals(ArcadeAnimationData.Facing.SIDE_TO_CAMERA, data.face());

        assertEquals(-360.0F, shipped.get("side_somersault").mirrored().body().orElseThrow().get(ArcadeAnimationData.Target.ROTATION).getLast().post().z);
    }

    @Test
    @DisplayName("side on, the player turns to the camera, and wheels the way it goes")
    void sideTurnLooksAtTheCamera() {
        // a camera looking south, at a player crossing its screen from left to right, westwards
        float turn = ArcadeAnimationData.sideTurnToCamera(90.0F, 0.0F);
        assertEquals(90.0F, turn);
        assertEquals(-1.0F, facingDotView(90.0F + turn, 0.0F), 1.0E-4F, "it faces the camera");
        // and from right to left, eastwards
        turn = ArcadeAnimationData.sideTurnToCamera(-90.0F, 0.0F);
        assertEquals(-90.0F, turn);
        assertEquals(-1.0F, facingDotView(-90.0F + turn, 0.0F), 1.0E-4F, "it faces the camera");
        // straight towards the camera or away from it: side on either way, always the same
        assertEquals(ArcadeAnimationData.sideTurnToCamera(180.0F, 0.0F), ArcadeAnimationData.sideTurnToCamera(-180.0F, 0.0F));
        assertEquals(ArcadeAnimationData.sideTurnToCamera(0.0F, 0.0F), ArcadeAnimationData.sideTurnToCamera(360.0F, 0.0F));
        for (float travel = -180.0F; travel < 180.0F; travel += 7.5F) {
            for (float view = -180.0F; view < 180.0F; view += 15.0F) {
                float side = ArcadeAnimationData.sideTurnToCamera(travel, view);
                assertEquals(90.0F, Math.abs(side), "a quarter turn");
                assertTrue(facingDotView(travel + side, view) <= 1.0E-3F, "turned away from the camera going " + travel + " seen towards " + view);
            }
        }
    }

    /** How much a player facing {@code facing} looks the way a camera looking towards {@code view} does: -1 when it faces the camera. */
    private static float facingDotView(float facing, float view) {
        return (float) Math.cos(Math.toRadians(facing - view));
    }

    @Test
    @DisplayName("a long jump flies feet first, legs reaching out level, arms swept back")
    void longJumpFliesFeetFirst() {
        var animation = shipped.get("long_jump");
        float body = animation.body().orElseThrow().get(ArcadeAnimationData.Target.ROTATION).get(1).post().x;
        assertTrue(body < -15.0F && body > -45.0F, "the chest leans forwards, but stays up, got " + body);
        for (var side : List.of("right", "left")) {
            float leg = animation.bones().get(side + "_leg").get(ArcadeAnimationData.Target.ROTATION).get(1).post().x;
            // the leg hangs along the leaning body: swung forwards from there, how far up from straight down it reaches
            float reach = body - leg;
            assertTrue(reach >= 65.0F && reach <= 90.0F, "the " + side + " leg reaches out ahead, close to level, got " + reach + " degrees up from straight down");
            float arm = animation.bones().get(side + "_arm").get(ArcadeAnimationData.Target.ROTATION).get(1).post().x;
            assertTrue(arm > 0.0F, "the " + side + " arm is swept back, got " + arm);
        }
        var stretch = animation.body().orElseThrow().get(ArcadeAnimationData.Target.SCALE).getFirst().post();
        assertTrue(stretch.y > 1.0F && stretch.x < 1.0F, "it takes off stretched, got " + stretch);
    }

    private static JsonObject parse(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
}
