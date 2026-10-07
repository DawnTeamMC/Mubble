package fr.hugman.mubble.arcade.client;

import fr.hugman.mubble.arcade.network.ArcadePayloadTypes;
import fr.hugman.mubble.arcade.ArcadeProfiles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public final class ArcadeClientNetworking {
    private ArcadeClientNetworking() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ArcadePayloadTypes.ARCADE_CORRECTION, (payload, context) -> LocalArcadeDriver.onCorrection(payload));
        ClientPlayNetworking.registerGlobalReceiver(ArcadePayloadTypes.ARCADE_PROFILES, (payload, context) -> ArcadeProfiles.setClientValues(payload.profiles()));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ArcadeProfiles.clearClientValues());
    }
}
