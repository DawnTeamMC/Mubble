package fr.hugman.mubble.client.arcade.compat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/**
 * The arcade actions as a controller mod sees them. Controlify fills this in through its entrypoint
 * when it is installed; without it, this stays empty and only the keys count.
 */
@Environment(EnvType.CLIENT)
public interface ArcadeControllerBindings {
    /** The actions held on the current controller, as {@link fr.hugman.mubble.world.arcade.sim.ArcadeInputFrame} bits. */
    int held();

    /** The actions pressed on the current controller since the previous call. */
    int pressed();

    /** Whether recenter was pressed since the previous call. */
    boolean recenterPressed();

    /** The glyph of the button bound to {@code action} on the current controller, if any. */
    @Nullable
    Component glyph(String action);

    final class Holder {
        @Nullable
        public static ArcadeControllerBindings instance;

        private Holder() {
        }
    }
}
