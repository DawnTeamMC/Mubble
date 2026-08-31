package fr.hugman.mubble.super_mario.client.references;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import org.joml.Quaternionfc;

@Environment(EnvType.CLIENT)
public class SuperMarioRenderStateDataKeys {
    /** Set on entities held inside a {@link fr.hugman.mubble.super_mario.world.entity.projectile.Bubble}. */
    public static final RenderStateDataKey<BubbleRide> BUBBLE_RIDE = RenderStateDataKey.create(() -> "Bubble ride");

    /**
     * How an entity held inside a bubble is drawn: tumbling around, and shrinking away while the bubble
     * swallows it.
     *
     * @param rotation the tumble to apply around the entity's middle
     * @param scale    1 while merely trapped, going down to 0 over the course of a capture
     */
    public record BubbleRide(Quaternionfc rotation, float scale) {
    }
}
