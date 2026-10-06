package fr.hugman.mubble.splatoon.client.network;

import fr.hugman.mubble.splatoon.client.ink.ClientInk;
import fr.hugman.mubble.splatoon.network.protocol.common.custom.SplatoonPayloadTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SplatoonClientPayloadReceivers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(SplatoonPayloadTypes.INK_SYNC, (payload, context) -> {
            var level = context.client().level;
            if (level != null) {
                ClientInk.apply(level, payload);
            }
        });
    }
}
