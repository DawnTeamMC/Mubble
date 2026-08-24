package fr.hugman.mubble.super_mario.network.protocol;

import fr.hugman.mubble.super_mario.network.protocol.common.custom.SuperMarioPayloadTypes;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class SuperMarioServerReceivers {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(SuperMarioPayloadTypes.STRUGGLE_FREE, (payload, context) ->
                context.server().execute(() -> Freezing.struggle(context.player())));
    }
}
