package fr.hugman.mubble.client.keybind;

import fr.hugman.mubble.client.power_up.PowerUpKeybindsHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class MubbleKeyBindings {
    public static final KeyBinding TRIGGER_POWER_UP = of(GLFW.GLFW_KEY_R, "key.mubble.trigger_power_up", KeyBinding.GAMEPLAY_CATEGORY);

    public static KeyBinding of(int code, String translationKey, String categoryTranslationKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(translationKey, InputUtil.Type.KEYSYM, code, categoryTranslationKey));
    }

    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(PowerUpKeybindsHandler::tick);
    }
}
