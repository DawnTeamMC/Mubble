package fr.hugman.mubble.arcade.client;

import com.mojang.blaze3d.platform.InputConstants;
import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * The keys of the arcade movement layer.
 * <p>
 * Most of the layer plays on the vanilla keys, so that they show in the controls menu with their own
 * defaults and never clash: jump and sneak, sprint, and attack and use, which roll when crouch is
 * held and dive out of a ground pound, see {@link ArcadeHands}. Only what vanilla has no key for is
 * here: the spin, recentering the camera, and the debug HUD.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeKeyMappings {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Mubble.id("arcade"));

    // B: free in vanilla and in Mubble (R triggers power-ups), at the same place on QWERTY and AZERTY
    public static final KeyMapping SPIN = register("arcade_spin", GLFW.GLFW_KEY_B);
    // unbound: no key left near the movement keys that vanilla or Mubble does not already use
    public static final KeyMapping RECENTER = register("arcade_recenter", InputConstants.UNKNOWN.getValue());
    public static final KeyMapping DEBUG_HUD = register("arcade_debug_hud", InputConstants.UNKNOWN.getValue());

    private ArcadeKeyMappings() {
    }

    private static KeyMapping register(String name, int key) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Mubble.MOD_ID + "." + name, InputConstants.Type.KEYSYM, key, CATEGORY));
    }

    public static void init() {
    }
}
