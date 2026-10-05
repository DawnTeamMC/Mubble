package fr.hugman.mubble.arcade.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.AnimationState;
import org.jspecify.annotations.Nullable;

/**
 * What the render state of a player carries about its arcade animation: the animation to play, and
 * when it started. {@link #NONE} when no arcade move is playing, which leaves the model alone.
 */
@Environment(EnvType.CLIENT)
public record ArcadeRenderData(@Nullable ArcadeAnimation animation, AnimationState state) {
    public static final ArcadeRenderData NONE = new ArcadeRenderData(null, new AnimationState());

    public boolean isPlaying() {
        return this.animation != null;
    }
}
