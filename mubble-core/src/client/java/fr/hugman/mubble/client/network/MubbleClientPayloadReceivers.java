package fr.hugman.mubble.client.network;

import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class MubbleClientPayloadReceivers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.POWER_UP_CHANGE, PowerUpChangePayloadReceiver.INSTANCE);
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.COLLECT_COLLECTIBLE, CollectCollectiblePayloadReceiver.INSTANCE);
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.ACTIVE_ENVIRONMENT, ActiveEnvironmentPayloadReceiver.INSTANCE);
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.ENVIRONMENT_PROFILE_SYNC, EnvironmentProfileSyncPayloadReceiver.INSTANCE);

        // A fresh connection must not inherit the previous server's reloaded profiles.
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientEnvironmentProfiles.clear());
    }
}
