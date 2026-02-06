package fr.hugman.mubble.splatoon.client;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.splatoon.client.model.SplatoonModelLayers;
import fr.hugman.mubble.splatoon.client.renderer.SplatoonRenderers;
import net.fabricmc.api.ClientModInitializer;

public class SplatoonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(SplatoonModelLayers.class);

        SplatoonRenderers.registerEntities();
    }
}
