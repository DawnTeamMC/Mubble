package fr.hugman.mubble.client.arcade;

import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import fr.hugman.mubble.world.arcade.ArcadeProfiles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public final class ArcadeClientNetworking {
    private ArcadeClientNetworking() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.ARCADE_CORRECTION, (payload, context) -> LocalArcadeDriver.onCorrection(payload));
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.ARCADE_PROFILES, (payload, context) -> ArcadeProfiles.setClientValues(payload.profiles()));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ArcadeProfiles.clearClientValues());
    }
}
