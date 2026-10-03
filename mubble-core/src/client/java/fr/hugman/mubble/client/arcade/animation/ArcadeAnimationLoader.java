package fr.hugman.mubble.client.arcade.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import fr.hugman.mubble.Mubble;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

/**
 * Loads the arcade animations from {@code assets/<namespace>/animations/arcade/<path>.json}, the id
 * of the animation being {@code <namespace>:<path>}: the animation of {@code mubble:backflip} lives in
 * {@code assets/mubble/animations/arcade/backflip.json}. Reloaded with the other resources, F3+T
 * included.
 * <pre>{@code
 * {
 *   "length": 0.6,                       // seconds
 *   "loop": false,
 *   "linger": false,                     // keep playing to the end when the next move has no animation
 *   "axes": "java",                      // "bedrock" for values written the way Bedrock files are
 *   "bones": {
 *     "right_arm": {
 *       "rotation": { "0.0": [0, 0, 0], "0.3": { "post": [-160, 0, 20], "lerp_mode": "catmullrom" } },
 *       "position": [ { "time": 0.0, "value": [0, 0, 0] } ],
 *       "scale": { "0.0": [1, 1, 1] }
 *     }
 *   },
 *   "body": {                            // the whole body, through the pose stack
 *     "pivot": [0, 0.9, 0],              // blocks above the feet
 *     "rotation": { "0.0": [0, 0, 0], "0.6": [-360, 0, 0] }
 *   }
 * }
 * }</pre>
 * Rotations are in degrees, positions in model pixels, scales as factors: the numbers
 * {@link KeyframeAnimations#degreeVec}, {@link KeyframeAnimations#posVec} and
 * {@link KeyframeAnimations#scaleVec} take, which is what Blockbench writes in its Java animations.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeAnimationLoader extends SimplePreparableReloadListener<Map<Identifier, ArcadeAnimation>> {
    private static final Logger LOGGER = LogManager.getLogger(Mubble.MOD_ID);
    public static final FileToIdConverter LISTER = FileToIdConverter.json("animations/arcade");

    @Override
    protected Map<Identifier, ArcadeAnimation> prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, ArcadeAnimation> animations = new HashMap<>();
        for (var entry : LISTER.listMatchingResources(manager).entrySet()) {
            var id = LISTER.fileToId(entry.getKey());
            try (Reader reader = entry.getValue().openAsReader()) {
                animations.put(id, parse(id, JsonParser.parseReader(reader).getAsJsonObject()));
            } catch (Exception exception) {
                LOGGER.error("Could not load the arcade animation {} from {}", id, entry.getKey(), exception);
            }
        }
        return animations;
    }

    @Override
    protected void apply(Map<Identifier, ArcadeAnimation> animations, ResourceManager manager, ProfilerFiller profiler) {
        ArcadeAnimations.set(animations);
    }

    public static ArcadeAnimation parse(Identifier id, JsonObject json) {
        float length = GsonHelper.getAsFloat(json, "length");
        boolean loop = GsonHelper.getAsBoolean(json, "loop", false);
        boolean bedrock = GsonHelper.getAsString(json, "axes", "java").equals("bedrock");

        var limbs = AnimationDefinition.Builder.withLength(length);
        if (loop) {
            limbs.looping();
        }
        var bones = GsonHelper.getAsJsonObject(json, "bones", new JsonObject());
        for (var bone : bones.entrySet()) {
            addChannels(limbs, bone.getKey(), GsonHelper.convertToJsonObject(bone.getValue(), bone.getKey()), bedrock);
        }

        Optional<AnimationDefinition> body = Optional.empty();
        var pivot = new Vector3f(0.0F, 0.9F, 0.0F);
        if (json.has("body")) {
            var bodyJson = GsonHelper.getAsJsonObject(json, "body");
            var builder = AnimationDefinition.Builder.withLength(length);
            if (loop) {
                builder.looping();
            }
            addChannels(builder, ArcadeAnimation.BODY_BONE, bodyJson, bedrock);
            body = Optional.of(builder.build());
            if (bodyJson.has("pivot")) {
                pivot = vector(GsonHelper.getAsJsonArray(bodyJson, "pivot"));
            }
        }
        return new ArcadeAnimation(id, limbs.build(), body, pivot, GsonHelper.getAsBoolean(json, "linger", false));
    }

    private static void addChannels(AnimationDefinition.Builder builder, String bone, JsonObject json, boolean bedrock) {
        for (var target : List.of("rotation", "position", "scale")) {
            if (json.has(target)) {
                var keyframes = keyframes(json.get(target), target, bedrock);
                if (!keyframes.isEmpty()) {
                    builder.addAnimation(bone, new AnimationChannel(channelTarget(target), keyframes.toArray(Keyframe[]::new)));
                }
            }
        }
    }

    private static AnimationChannel.Target channelTarget(String target) {
        return switch (target) {
            case "rotation" -> AnimationChannel.Targets.ROTATION;
            case "position" -> AnimationChannel.Targets.POSITION;
            default -> AnimationChannel.Targets.SCALE;
        };
    }

    /** Keyframes written either as a list of {@code {time, value}} or as an object keyed by time, sorted by time. */
    private static List<Keyframe> keyframes(JsonElement element, String target, boolean bedrock) {
        List<Keyframe> keyframes = new ArrayList<>();
        if (element.isJsonArray() && !isVector(element.getAsJsonArray())) {
            for (var frame : element.getAsJsonArray()) {
                var object = GsonHelper.convertToJsonObject(frame, "keyframe");
                keyframes.add(keyframe(GsonHelper.getAsFloat(object, "time"), object.has("value") ? object.get("value") : object, target, bedrock));
            }
        } else if (element.isJsonObject()) {
            for (var frame : element.getAsJsonObject().entrySet()) {
                keyframes.add(keyframe(Float.parseFloat(frame.getKey()), frame.getValue(), target, bedrock));
            }
        } else {
            // a single value held all along
            keyframes.add(keyframe(0.0F, element, target, bedrock));
        }
        keyframes.sort(Comparator.comparingDouble(Keyframe::timestamp));
        return keyframes;
    }

    private static Keyframe keyframe(float time, JsonElement value, String target, boolean bedrock) {
        var interpolation = AnimationChannel.Interpolations.LINEAR;
        Vector3f pre;
        Vector3f post;
        if (value.isJsonObject()) {
            var object = value.getAsJsonObject();
            var mode = GsonHelper.getAsString(object, "lerp_mode", GsonHelper.getAsString(object, "interpolation", "linear"));
            if (mode.equals("catmullrom")) {
                interpolation = AnimationChannel.Interpolations.CATMULLROM;
            }
            var postJson = object.has("post") ? object.get("post") : object.has("vector") ? object.get("vector") : object.get("value");
            if (postJson == null) {
                throw new JsonParseException("A keyframe needs a 'post', 'vector' or 'value'");
            }
            post = convert(vector(postJson.getAsJsonArray()), target, bedrock);
            pre = object.has("pre") ? convert(vector(GsonHelper.getAsJsonArray(object, "pre")), target, bedrock) : post;
        } else {
            post = convert(vector(value.getAsJsonArray()), target, bedrock);
            pre = post;
        }
        return new Keyframe(time, pre, post, interpolation);
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

    /** Turns authored numbers into what the animation channels apply. */
    private static Vector3f convert(Vector3f value, String target, boolean bedrock) {
        return switch (target) {
            case "rotation" -> bedrock
                    ? KeyframeAnimations.degreeVec(-value.x, -value.y, value.z)
                    : KeyframeAnimations.degreeVec(value.x, value.y, value.z);
            case "position" -> bedrock
                    ? KeyframeAnimations.posVec(-value.x, value.y, value.z)
                    : KeyframeAnimations.posVec(value.x, value.y, value.z);
            default -> KeyframeAnimations.scaleVec(value.x, value.y, value.z);
        };
    }
}
