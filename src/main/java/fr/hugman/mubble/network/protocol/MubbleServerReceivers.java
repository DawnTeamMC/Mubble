package fr.hugman.mubble.network.protocol;

import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class MubbleServerReceivers {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.POWER_UP_TRIGGER, ((payload, context) -> context.server().execute(() ->
                context.player().getPowerUp().ifPresent(entry -> entry.value().trigger(context.player()))
        )));
    }
}
