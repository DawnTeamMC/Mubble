package fr.hugman.mubble.arcade;

import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

/**
 * Runs the step of the player a client controls, in place of vanilla travel. Set by the client
 * initializer: a dedicated server has no player of its own to drive.
 */
public interface ArcadeLocalDriver {
    void travel(Player player, ArcadeController controller);

    final class Holder {
        @Nullable
        public static ArcadeLocalDriver instance;

        private Holder() {
        }
    }
}
