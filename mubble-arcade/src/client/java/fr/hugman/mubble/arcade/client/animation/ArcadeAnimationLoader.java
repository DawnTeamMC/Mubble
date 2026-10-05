package fr.hugman.mubble.arcade.client.animation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.animation.ArcadeAnimationData;
import java.io.Reader;
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
        var data = ArcadeAnimationData.parse(json);
        var limbs = AnimationDefinition.Builder.withLength(data.length());
        if (data.loop()) {
            limbs.looping();
        }
        data.bones().forEach((bone, channels) -> addChannels(limbs, bone, channels, data.bedrock()));

        Optional<AnimationDefinition> body = data.body().map(channels -> {
            var builder = AnimationDefinition.Builder.withLength(data.length());
            if (data.loop()) {
                builder.looping();
            }
            addChannels(builder, ArcadeAnimation.BODY_BONE, channels, data.bedrock());
            return builder.build();
        });
        return new ArcadeAnimation(id, limbs.build(), body, new Vector3f(data.pivot()), data.linger());
    }

    private static void addChannels(AnimationDefinition.Builder builder, String bone, Map<ArcadeAnimationData.Target, List<ArcadeAnimationData.Frame>> channels, boolean bedrock) {
        channels.forEach((target, frames) -> {
            var keyframes = frames.stream().map(frame -> keyframe(frame, target, bedrock)).toArray(Keyframe[]::new);
            builder.addAnimation(bone, new AnimationChannel(channelTarget(target), keyframes));
        });
    }

    private static AnimationChannel.Target channelTarget(ArcadeAnimationData.Target target) {
        return switch (target) {
            case ROTATION -> AnimationChannel.Targets.ROTATION;
            case POSITION -> AnimationChannel.Targets.POSITION;
            case SCALE -> AnimationChannel.Targets.SCALE;
        };
    }

    private static Keyframe keyframe(ArcadeAnimationData.Frame frame, ArcadeAnimationData.Target target, boolean bedrock) {
        var interpolation = frame.smooth() ? AnimationChannel.Interpolations.CATMULLROM : AnimationChannel.Interpolations.LINEAR;
        return new Keyframe(frame.time(), convert(frame.pre(), target, bedrock), convert(frame.post(), target, bedrock), interpolation);
    }

    /** Turns authored numbers into what the animation channels apply. */
    private static Vector3f convert(Vector3f value, ArcadeAnimationData.Target target, boolean bedrock) {
        return switch (target) {
            case ROTATION -> bedrock
                    ? KeyframeAnimations.degreeVec(-value.x, -value.y, value.z)
                    : KeyframeAnimations.degreeVec(value.x, value.y, value.z);
            case POSITION -> bedrock
                    ? KeyframeAnimations.posVec(-value.x, value.y, value.z)
                    : KeyframeAnimations.posVec(value.x, value.y, value.z);
            case SCALE -> KeyframeAnimations.scaleVec(value.x, value.y, value.z);
        };
    }
}
