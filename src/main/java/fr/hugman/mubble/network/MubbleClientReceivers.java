package fr.hugman.mubble.network;

import fr.hugman.mubble.network.payload.MubblePayloads;
import fr.hugman.mubble.power_up.PowerUp;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class MubbleClientReceivers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloads.POWER_UP_CHANGE, ((payload, context) -> context.client().execute(() -> {
            PowerUp.onChange(context.player(), payload.previous(), payload.next());
        })));
    }
}
