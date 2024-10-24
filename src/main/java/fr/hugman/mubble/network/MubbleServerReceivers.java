package fr.hugman.mubble.network;

import fr.hugman.mubble.network.payload.MubblePayloads;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class MubbleServerReceivers {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(MubblePayloads.POWER_UP_TRIGGER, ((payload, context) -> context.server().execute(() ->
                context.player().getPowerUp().ifPresent(entry -> entry.value().trigger(context.server(), context.player()))
        )));
        ServerPlayNetworking.registerGlobalReceiver(MubblePayloads.POWER_UP_JUMP_TRIGGER, ((payload, context) -> context.server().execute(() ->
                context.player().getPowerUp().ifPresent(entry -> entry.value().jumpTrigger(context.server(), context.player()))
        )));
    }
}
