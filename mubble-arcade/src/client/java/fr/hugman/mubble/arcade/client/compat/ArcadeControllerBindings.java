package fr.hugman.mubble.arcade.client.compat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

/**
 * What the arcade layer needs from a controller mod besides the vanilla keys, which the mod presses
 * itself. Controlify fills this in through its entrypoint when it is installed; without it, this
 * stays empty and only the keys count.
 */
@Environment(EnvType.CLIENT)
public interface ArcadeControllerBindings {
    /** The actions held on the current controller that no key carries, as {@link fr.hugman.mubble.arcade.sim.ArcadeInputFrame} bits. */
    int held();

    /** Rumbles the current controller, if the player plays on one, see {@link fr.hugman.mubble.arcade.cue.Cue.Rumble}. */
    void rumble(float strong, float weak, int ticks);

    final class Holder {
        @Nullable
        public static ArcadeControllerBindings instance;

        private Holder() {
        }
    }
}
