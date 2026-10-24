package fr.hugman.mubble.platformer.client;

import fr.hugman.mubble.platformer.network.PlatformerPayloadTypes;
import fr.hugman.mubble.platformer.PlatformerProfiles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public final class PlatformerClientNetworking {
    private PlatformerClientNetworking() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PlatformerPayloadTypes.PLATFORMER_CORRECTION, (payload, context) -> LocalPlatformerDriver.onCorrection(payload));
        ClientPlayNetworking.registerGlobalReceiver(PlatformerPayloadTypes.PLATFORMER_PROFILES, (payload, context) -> PlatformerProfiles.setClientValues(payload.profiles()));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> PlatformerProfiles.clearClientValues());
    }
}
