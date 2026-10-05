package fr.hugman.mubble.client.compat.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;
import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.input.ButtonInput;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.client.keybind.MubbleKeyBindings;
import fr.hugman.mubble.keybind.MubbleKeyBindingsKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * The keys of Mubble on a controller, when Controlify is installed: each gets a binding of its own,
 * which presses the key. Controlify would otherwise add a binding of its own for each, unbound.
 */
@Environment(EnvType.CLIENT)
public final class MubbleControlifyEntrypoint implements ControlifyEntrypoint {
    @Override
    public void onControlifyPreInit(PreInitContext context) {
        context.bindings().registerBinding(builder -> builder
                .id(Mubble.id("trigger_power_up"))
                .name(Component.translatable(MubbleKeyBindingsKeys.TRIGGER_POWER_UP))
                .category(KeyMapping.Category.GAMEPLAY.label())
                .allowedContexts(BindContext.IN_GAME)
                .keyEmulation(MubbleKeyBindings.TRIGGER_POWER_UP)
                // B, the one button Controlify leaves free in game; a resource pack may move it, as the arcade movement does
                .defaultInput(new ButtonInput(Identifier.fromNamespaceAndPath("controlify", "button/east"))));
    }

    @Override
    public void onControlifyInit(InitContext context) {
    }

    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {
    }
}
