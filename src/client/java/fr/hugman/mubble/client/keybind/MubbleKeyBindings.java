package fr.hugman.mubble.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class MubbleKeyBindings {
    public static final KeyMapping TRIGGER_POWER_UP = register(GLFW.GLFW_KEY_R, MubbleKeyBindingsKeys.TRIGGER_POWER_UP, KeyMapping.Category.GAMEPLAY);

    public static KeyMapping register(int code, String translationKey, KeyMapping.Category categoryTranslationKey) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(translationKey, InputConstants.Type.KEYSYM, code, categoryTranslationKey));
    }

    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(PowerUpKeybindsHandler::tick);
    }
}
