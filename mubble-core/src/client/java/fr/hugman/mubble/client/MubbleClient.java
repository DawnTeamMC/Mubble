package fr.hugman.mubble.client;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.client.keybind.MubbleKeyBindings;
import fr.hugman.mubble.client.model.MubbleModelLayers;
import fr.hugman.mubble.client.network.MubbleClientReceivers;
import fr.hugman.mubble.client.renderer.MubbleRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MubbleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(MubbleModelLayers.class);

        MubbleKeyBindings.registerEvents();
        MubbleClientReceivers.register();
    }
}
