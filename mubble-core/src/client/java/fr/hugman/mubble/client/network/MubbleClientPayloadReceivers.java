package fr.hugman.mubble.client.network;

import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class MubbleClientPayloadReceivers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.POWER_UP_CHANGE, PowerUpChangePayloadReceiver.INSTANCE);
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.COLLECT_COLLECTIBLE, CollectCollectiblePayloadReceiver.INSTANCE);
    }
}
