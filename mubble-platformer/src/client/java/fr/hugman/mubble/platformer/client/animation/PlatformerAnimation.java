package fr.hugman.mubble.platformer.client.animation;

import fr.hugman.mubble.platformer.animation.PlatformerAnimationData;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

/**
 * The animation of one platformer move, as loaded from a resource pack.
 *
 * @param id     where it was loaded from
 * @param limbs  the keyframes of the bones of the player model, played with the vanilla keyframe
 *               animation API
 * @param body   the keyframes of the whole body, applied to the pose stack around {@code pivot}: flips,
 *               rolls, tilts
 * @param pivot  the point the whole body turns around, in blocks above the feet
 * @param linger  whether the animation keeps playing to its end when the move it belongs to gives way
 *                to a move without an animation of its own: landings, skids, pull-ups
 * @param blendIn how long the model takes to ease from the pose before into this animation, in seconds
 * @param face    which way the body faces while it plays
 * @param mirror  the same animation left for right, for an animation played side on, which plays it
 *                mirrored when the player turns its right side to its path
 */
@Environment(EnvType.CLIENT)
public record PlatformerAnimation(Identifier id, AnimationDefinition limbs, Optional<AnimationDefinition> body, Vector3f pivot, boolean linger, float blendIn,
                              PlatformerAnimationData.Facing face, @Nullable PlatformerAnimation mirror) {
    /** The name of the only bone of the whole body channel. */
    public static final String BODY_BONE = "body_transform";
    /** A model of a single bone, which the whole body channel animates to read the values back. */
    static final ModelPart BODY_ROOT = new ModelPart(List.of(), Map.of(BODY_BONE, new ModelPart(List.of(), Map.of())));
    static final ModelPart BODY_PART = BODY_ROOT.getChild(BODY_BONE);

    public float lengthInSeconds() {
        return Math.max(this.limbs.lengthInSeconds(), this.body.map(AnimationDefinition::lengthInSeconds).orElse(0.0F));
    }

    public boolean looping() {
        return this.limbs.looping();
    }

    /**
     * This animation as it plays after the quarter turn {@code turn} of {@link #face}: itself when the
     * path is to the left of the player, its mirror image when it is to the right.
     */
    public PlatformerAnimation turned(float turn) {
        return turn < 0.0F && this.mirror != null ? this.mirror : this;
    }

    /** The whole body channel baked against its single bone, once. */
    KeyframeAnimation bakedBody() {
        return this.body.orElseThrow().bake(BODY_ROOT);
    }
}
