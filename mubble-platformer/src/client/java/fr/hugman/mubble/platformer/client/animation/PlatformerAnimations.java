package fr.hugman.mubble.platformer.client.animation;

import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

/**
 * The platformer animations currently loaded.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerAnimations {
    private static Map<Identifier, PlatformerAnimation> animations = Map.of();

    private PlatformerAnimations() {
    }

    public static Optional<PlatformerAnimation> get(Identifier id) {
        return Optional.ofNullable(animations.get(id));
    }

    public static Map<Identifier, PlatformerAnimation> all() {
        return animations;
    }

    static void set(Map<Identifier, PlatformerAnimation> loaded) {
        animations = Map.copyOf(loaded);
        PlatformerPlayerAnimator.clearBakedCache();
    }
}
