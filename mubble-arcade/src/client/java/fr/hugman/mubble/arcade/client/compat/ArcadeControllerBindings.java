package fr.hugman.mubble.arcade.client.compat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.Vec2;
import org.jspecify.annotations.Nullable;

/**
 * What the arcade layer needs from a controller mod besides its keys, which the mod presses itself.
 * Controlify fills this in through its entrypoint when it is installed; without it, this stays empty
 * and only the keys count.
 */
@Environment(EnvType.CLIENT)
public interface ArcadeControllerBindings {
    /** The movement stick of the current controller, relative to the view: x to the left, y forward. */
    Vec2 stick();

    /** Whether the player plays on a controller right now, rather than the keyboard and mouse. */
    boolean usingController();

    /** Rumbles the current controller, if the player plays on one, see {@link fr.hugman.mubble.arcade.cue.Cue.Rumble}. */
    void rumble(float strong, float weak, int ticks);

    final class Holder {
        @Nullable
        public static ArcadeControllerBindings instance;

        private Holder() {
        }
    }
}
