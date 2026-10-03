package fr.hugman.mubble.client.arcade.animation;

import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

/**
 * The arcade animations currently loaded.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeAnimations {
    private static Map<Identifier, ArcadeAnimation> animations = Map.of();

    private ArcadeAnimations() {
    }

    public static Optional<ArcadeAnimation> get(Identifier id) {
        return Optional.ofNullable(animations.get(id));
    }

    public static Map<Identifier, ArcadeAnimation> all() {
        return animations;
    }

    static void set(Map<Identifier, ArcadeAnimation> loaded) {
        animations = Map.copyOf(loaded);
        ArcadePlayerAnimator.clearBakedCache();
    }
}
