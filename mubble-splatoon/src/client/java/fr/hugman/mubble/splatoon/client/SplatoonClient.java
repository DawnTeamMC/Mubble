package fr.hugman.mubble.splatoon.client;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.splatoon.client.ink.ClientInk;
import fr.hugman.mubble.splatoon.client.model.SplatoonModelLayers;
import fr.hugman.mubble.splatoon.client.network.SplatoonClientPayloadReceivers;
import fr.hugman.mubble.splatoon.client.renderer.SplatoonRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class SplatoonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Reflection.initialize(SplatoonModelLayers.class);

        SplatoonRenderers.registerEntities();
        SplatoonClientPayloadReceivers.register();

        ClientChunkEvents.CHUNK_UNLOAD.register((level, chunk) -> ClientInk.forgetChunk(level, chunk.getPos()));
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((client, level) -> ClientInk.clear());
        ClientPlayConnectionEvents.DISCONNECT.register((listener, client) -> ClientInk.clear());
    }
}
