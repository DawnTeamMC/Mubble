package fr.hugman.mubble.platformer;

import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

/**
 * Runs the step of the player a client controls, in place of vanilla travel. Set by the client
 * initializer: a dedicated server has no player of its own to drive.
 */
public interface PlatformerLocalDriver {
    void travel(Player player, PlatformerController controller);

    final class Holder {
        @Nullable
        public static PlatformerLocalDriver instance;

        private Holder() {
        }
    }
}
