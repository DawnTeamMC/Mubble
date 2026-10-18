package fr.hugman.mubble.zelda.client;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.zelda.client.model.ZeldaModelLayers;
import fr.hugman.mubble.zelda.client.renderer.ZeldaRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ZeldaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(ZeldaModelLayers.class);

        ZeldaRenderers.registerEntities();
    }
}
