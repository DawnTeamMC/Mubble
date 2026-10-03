package fr.hugman.mubble.fortnite.client;

import fr.hugman.mubble.fortnite.client.renderer.FortniteRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FortniteClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FortniteRenderers.registerEntities();
    }
}
