package fr.hugman.mubble.arcade.client;

import com.mojang.blaze3d.platform.InputConstants;
import fr.hugman.mubble.Mubble;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * The keys of the arcade movement layer, every one of them its own, so that the layer can be played
 * on other keys than vanilla's: moving, jumping, crouching, sprinting, attacking and using items,
 * which roll when crouch is held and dive out of a ground pound (see {@link ArcadeHands}), spinning,
 * recentering the camera, and the debug HUD.
 * <p>
 * The basic ones default to the keys of their vanilla counterparts. Vanilla does not flag two keys
 * left on their defaults as a conflict, and the two never act at once: while the layer drives, the
 * vanilla ones stand aside, see {@link ArcadeClientInput}; the rest of the time, these do nothing.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeKeyMappings {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Mubble.id("arcade"));

    public static final KeyMapping FORWARD = register("arcade_forward", GLFW.GLFW_KEY_W);
    public static final KeyMapping BACKWARD = register("arcade_backward", GLFW.GLFW_KEY_S);
    public static final KeyMapping LEFT = register("arcade_left", GLFW.GLFW_KEY_A);
    public static final KeyMapping RIGHT = register("arcade_right", GLFW.GLFW_KEY_D);
    public static final KeyMapping JUMP = register("arcade_jump", GLFW.GLFW_KEY_SPACE);
    // a second key, for the second jump button of a controller
    public static final KeyMapping JUMP_ALT = register("arcade_jump_alt", InputConstants.UNKNOWN.getValue());
    public static final KeyMapping CROUCH = register("arcade_crouch", GLFW.GLFW_KEY_LEFT_SHIFT);
    public static final KeyMapping SPRINT = register("arcade_sprint", GLFW.GLFW_KEY_LEFT_CONTROL);
    public static final KeyMapping ATTACK = register("arcade_attack", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT);
    public static final KeyMapping USE = register("arcade_use", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    // B: free in vanilla and in Mubble (R triggers power-ups), at the same place on QWERTY and AZERTY
    public static final KeyMapping SPIN = register("arcade_spin", GLFW.GLFW_KEY_B);
    // unbound: no key left near the movement keys that vanilla or Mubble does not already use
    public static final KeyMapping RECENTER = register("arcade_recenter", InputConstants.UNKNOWN.getValue());
    public static final KeyMapping DEBUG_HUD = register("arcade_debug_hud", InputConstants.UNKNOWN.getValue());

    /** The keys that only mean something while the layer drives. */
    public static final List<KeyMapping> DRIVING = List.of(FORWARD, BACKWARD, LEFT, RIGHT, JUMP, JUMP_ALT, CROUCH, SPRINT, ATTACK, USE, SPIN, RECENTER);

    private ArcadeKeyMappings() {
    }

    private static KeyMapping register(String name, int key) {
        return register(name, InputConstants.Type.KEYSYM, key);
    }

    private static KeyMapping register(String name, InputConstants.Type type, int key) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Mubble.MOD_ID + "." + name, type, key, CATEGORY));
    }

    public static void init() {
    }
}
