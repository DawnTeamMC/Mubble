package fr.hugman.mubble.arcade.client;

import com.mojang.blaze3d.platform.InputConstants;
import fr.hugman.mubble.Mubble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * The keys of the arcade movement layer.
 * <p>
 * Defaults are picked not to clash with vanilla nor with the rest of Mubble: jump and crouch are left
 * unbound, which means "use the vanilla jump and sneak keys" (space and left shift by default); action
 * is on left alt and recenter on Z, neither of which vanilla uses on its own. Spin and the debug HUD
 * are unbound.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeKeyMappings {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Mubble.id("arcade"));

    public static final KeyMapping JUMP = register("arcade_jump", InputConstants.UNKNOWN.getValue());
    public static final KeyMapping CROUCH = register("arcade_crouch", InputConstants.UNKNOWN.getValue());
    // B: free in vanilla and in Mubble (R triggers power-ups), at the same place on QWERTY and AZERTY;
    // not Alt, as Shift + Alt switches the keyboard layout on Windows and crouch + action is the roll
    public static final KeyMapping ACTION = register("arcade_action", GLFW.GLFW_KEY_B);
    public static final KeyMapping RECENTER = register("arcade_recenter", InputConstants.UNKNOWN.getValue());
    public static final KeyMapping SPIN = register("arcade_spin", InputConstants.UNKNOWN.getValue());
    public static final KeyMapping DEBUG_HUD = register("arcade_debug_hud", InputConstants.UNKNOWN.getValue());

    private ArcadeKeyMappings() {
    }

    private static KeyMapping register(String name, int key) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Mubble.MOD_ID + "." + name, InputConstants.Type.KEYSYM, key, CATEGORY));
    }

    public static void init() {
    }

    /** Our jump key when bound, the vanilla one otherwise. */
    public static KeyMapping jump(Minecraft minecraft) {
        return JUMP.isUnbound() ? minecraft.options.keyJump : JUMP;
    }

    /** Our crouch key when bound, the vanilla sneak key otherwise. */
    public static KeyMapping crouch(Minecraft minecraft) {
        return CROUCH.isUnbound() ? minecraft.options.keyShift : CROUCH;
    }
}
