package fr.hugman.mubble.client.network;

import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class MubbleClientReceivers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.POWER_UP_CHANGE, ((payload, context) -> context.client().execute(() -> {
            PowerUp.onChange(context.player(), payload.previous(), payload.next());
        })));
        ClientPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.COLLECT_COLLECTIBLE, CollectCollectiblePayloadReceiver.INSTANCE);
    }
}
