package fr.hugman.mubble.arcade.client.compat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

/**
 * The arcade actions as a controller mod sees them. Controlify fills this in through its entrypoint
 * when it is installed; without it, this stays empty and only the keys count.
 */
@Environment(EnvType.CLIENT)
public interface ArcadeControllerBindings {
    /** The actions held on the current controller, as {@link fr.hugman.mubble.arcade.sim.ArcadeInputFrame} bits. */
    int held();

    /** The actions pressed on the current controller since the previous call. */
    int pressed();

    /** Whether recenter was pressed since the previous call. */
    boolean recenterPressed();

    /** Rumbles the current controller, if the player plays on one, see {@link fr.hugman.mubble.arcade.cue.Cue.Rumble}. */
    void rumble(float strong, float weak, int ticks);

    final class Holder {
        @Nullable
        public static ArcadeControllerBindings instance;

        private Holder() {
        }
    }
}
