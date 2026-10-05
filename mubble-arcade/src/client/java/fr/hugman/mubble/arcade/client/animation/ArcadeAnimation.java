package fr.hugman.mubble.arcade.client.animation;

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

/**
 * The animation of one arcade move, as loaded from a resource pack.
 *
 * @param id     where it was loaded from
 * @param limbs  the keyframes of the bones of the player model, played with the vanilla keyframe
 *               animation API
 * @param body   the keyframes of the whole body, applied to the pose stack around {@code pivot}: flips,
 *               rolls, tilts
 * @param pivot  the point the whole body turns around, in blocks above the feet
 * @param linger whether the animation keeps playing to its end when the move it belongs to gives way
 *               to a move without an animation of its own: landings, skids, pull-ups
 */
@Environment(EnvType.CLIENT)
public record ArcadeAnimation(Identifier id, AnimationDefinition limbs, Optional<AnimationDefinition> body, Vector3f pivot, boolean linger) {
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

    /** The whole body channel baked against its single bone, once. */
    KeyframeAnimation bakedBody() {
        return this.body.orElseThrow().bake(BODY_ROOT);
    }
}
