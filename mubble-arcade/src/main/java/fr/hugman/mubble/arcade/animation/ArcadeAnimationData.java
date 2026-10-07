package fr.hugman.mubble.arcade.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.EasingType;
import net.minecraft.util.GsonHelper;
import org.joml.Vector3f;

/**
 * An arcade animation as authored, read from {@code assets/<namespace>/animations/arcade/<move>.json}.
 * The client turns it into vanilla keyframe animations; this side of it only reads and checks the
 * file, so that the files can be checked without a client.
 * <p>
 * The format:
 * <pre>{@code
 * {
 *   "length": 0.6,            // seconds
 *   "loop": false,
 *   "linger": false,          // keeps playing after the move ends, until its length is reached
 *   "blend_in": 0.12,         // seconds the model takes to ease from the pose before into this one
 *   "axes": "java",           // or "bedrock", for numbers exported from Blockbench
 *   "bones": {                // the parts of the player model: head, hat, body, arms and legs
 *     "right_arm": {
 *       "rotation": { "0.0": [0, 0, 0], "0.3": { "post": [-160, 0, 20], "easing": "out_back" } }
 *     }
 *   },
 *   "body": {                 // the whole body, turned around the pivot (in blocks from the feet)
 *     "pivot": [0, 0.9, 0],
 *     "rotation": [{ "time": 0.0, "value": [0, 0, 0] }, { "time": 0.6, "value": [-360, 0, 0], "easing": "in_out_sine" }],
 *     "scale": { "0.0": [1, 1, 1], "0.1": [1.15, 0.8, 1.15] }   // squash and stretch, from the feet
 *   }
 * }
 * }</pre>
 * Rotations are in degrees, positions in pixels, scales as factors. A channel is a list of
 * {@code {time, value}}, an object keyed by time, or a single value held all along; a keyframe value is
 * a vector, or an object with {@code post}, an optional {@code pre}, an optional {@code lerp_mode}
 * ({@code linear} or {@code catmullrom}) and an optional {@code easing}, which shapes the way to that
 * keyframe: any of vanilla's easing types ({@code in_sine}, {@code out_back}, {@code in_out_cubic},
 * {@code out_bounce}, {@code out_elastic}...), or {@code {"cubic_bezier": [x1, y1, x2, y2]}}.
 */
public record ArcadeAnimationData(float length, boolean loop, boolean linger, float blendIn, boolean bedrock,
                                  Map<String, Map<Target, List<Frame>>> bones, Optional<Map<Target, List<Frame>>> body, Vector3f pivot) {
    /** The bones a player model has, and so the only ones an animation may name. */
    public static final Set<String> PLAYER_BONES = Set.of("head", "hat", "body", "right_arm", "left_arm", "right_leg", "left_leg");
    private static final Vector3f DEFAULT_PIVOT = new Vector3f(0.0F, 0.9F, 0.0F);
    /** How long the model takes by default to ease from the pose before into an animation, in seconds. */
    public static final float DEFAULT_BLEND_IN = 0.12F;

    public enum Target {
        ROTATION("rotation"),
        POSITION("position"),
        SCALE("scale");

        private final String key;

        Target(String key) {
            this.key = key;
        }
    }

    /**
     * @param smooth whether the curve to this keyframe is a Catmull-Rom spline rather than a straight line
     * @param easing how the way to this keyframe goes over time
     */
    public record Frame(float time, Vector3f pre, Vector3f post, boolean smooth, EasingType easing) {
    }

    public static ArcadeAnimationData parse(JsonObject json) {
        float length = GsonHelper.getAsFloat(json, "length");
        if (length <= 0.0F) {
            throw new JsonParseException("An animation needs a positive length, got " + length);
        }
        boolean loop = GsonHelper.getAsBoolean(json, "loop", false);
        boolean linger = GsonHelper.getAsBoolean(json, "linger", false);
        float blendIn = GsonHelper.getAsFloat(json, "blend_in", DEFAULT_BLEND_IN);
        if (blendIn < 0.0F) {
            throw new JsonParseException("blend_in cannot be negative, got " + blendIn);
        }
        var axes = GsonHelper.getAsString(json, "axes", "java");
        if (!axes.equals("java") && !axes.equals("bedrock")) {
            throw new JsonParseException("Unknown axes '" + axes + "', expected 'java' or 'bedrock'");
        }

        Map<String, Map<Target, List<Frame>>> bones = new LinkedHashMap<>();
        for (var bone : GsonHelper.getAsJsonObject(json, "bones", new JsonObject()).entrySet()) {
            if (!PLAYER_BONES.contains(bone.getKey())) {
                throw new JsonParseException("Unknown bone '" + bone.getKey() + "', expected one of " + PLAYER_BONES);
            }
            bones.put(bone.getKey(), channels(GsonHelper.convertToJsonObject(bone.getValue(), bone.getKey()), length));
        }

        Optional<Map<Target, List<Frame>>> body = Optional.empty();
        var pivot = DEFAULT_PIVOT;
        if (json.has("body")) {
            var bodyJson = GsonHelper.getAsJsonObject(json, "body");
            body = Optional.of(channels(bodyJson, length));
            if (bodyJson.has("pivot")) {
                pivot = vector(GsonHelper.getAsJsonArray(bodyJson, "pivot"));
            }
        }
        return new ArcadeAnimationData(length, loop, linger, blendIn, axes.equals("bedrock"), bones, body, pivot);
    }

    private static Map<Target, List<Frame>> channels(JsonObject json, float length) {
        Map<Target, List<Frame>> channels = new EnumMap<>(Target.class);
        for (var target : Target.values()) {
            if (json.has(target.key)) {
                var frames = frames(json.get(target.key));
                for (var frame : frames) {
                    if (frame.time < 0.0F || frame.time > length + 1.0E-4F) {
                        throw new JsonParseException("A keyframe at " + frame.time + "s is outside of the animation, which lasts " + length + "s");
                    }
                }
                if (!frames.isEmpty()) {
                    channels.put(target, frames);
                }
            }
        }
        return channels;
    }

    private static List<Frame> frames(JsonElement element) {
        List<Frame> frames = new ArrayList<>();
        if (element.isJsonArray() && !isVector(element.getAsJsonArray())) {
            for (var frame : element.getAsJsonArray()) {
                var object = GsonHelper.convertToJsonObject(frame, "keyframe");
                frames.add(frame(GsonHelper.getAsFloat(object, "time"), object.has("value") ? object.get("value") : object));
            }
        } else if (element.isJsonObject()) {
            for (var frame : element.getAsJsonObject().entrySet()) {
                float time;
                try {
                    time = Float.parseFloat(frame.getKey());
                } catch (NumberFormatException e) {
                    throw new JsonParseException("Keyframes keyed by time need numbers as keys, got '" + frame.getKey() + "'");
                }
                frames.add(frame(time, frame.getValue()));
            }
        } else {
            // a single value held all along
            frames.add(frame(0.0F, element));
        }
        frames.sort(Comparator.comparingDouble(Frame::time));
        return frames;
    }

    private static Frame frame(float time, JsonElement value) {
        if (value.isJsonObject()) {
            var object = value.getAsJsonObject();
            var mode = GsonHelper.getAsString(object, "lerp_mode", GsonHelper.getAsString(object, "interpolation", "linear"));
            if (!mode.equals("linear") && !mode.equals("catmullrom")) {
                throw new JsonParseException("Unknown lerp_mode '" + mode + "', expected 'linear' or 'catmullrom'");
            }
            var postJson = object.has("post") ? object.get("post") : object.has("vector") ? object.get("vector") : object.get("value");
            if (postJson == null || !postJson.isJsonArray()) {
                throw new JsonParseException("A keyframe needs a 'post', 'vector' or 'value' of three numbers");
            }
            var post = vector(postJson.getAsJsonArray());
            var pre = object.has("pre") ? vector(GsonHelper.getAsJsonArray(object, "pre")) : post;
            var easing = object.has("easing")
                    ? EasingType.CODEC.parse(JsonOps.INSTANCE, object.get("easing")).getOrThrow(error -> new JsonParseException("Unknown easing " + object.get("easing") + ": " + error))
                    : EasingType.LINEAR;
            return new Frame(time, pre, post, mode.equals("catmullrom"), easing);
        }
        if (!value.isJsonArray()) {
            throw new JsonParseException("A keyframe value is three numbers, got " + value);
        }
        var vector = vector(value.getAsJsonArray());
        return new Frame(time, vector, vector, false, EasingType.LINEAR);
    }

    private static boolean isVector(JsonArray array) {
        return array.size() == 3 && array.get(0).isJsonPrimitive();
    }

    private static Vector3f vector(JsonArray array) {
        if (array.size() != 3) {
            throw new JsonParseException("Expected three numbers, got " + array);
        }
        return new Vector3f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
    }
}
