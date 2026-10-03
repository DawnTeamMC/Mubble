package fr.hugman.mubble.world.arcade.server;

import fr.hugman.mubble.network.protocol.common.custom.ArcadeProfilesPayload;
import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import fr.hugman.mubble.world.arcade.ArcadeProfiles;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class ArcadeServerNetworking {
    private ArcadeServerNetworking() {
    }

    public static void register() {
        // handled right away on the server thread, so that it keeps its place before the vanilla move packet of the same tick
        ServerPlayNetworking.registerGlobalReceiver(MubblePayloadTypes.ARCADE_INPUT, (payload, context) -> ArcadeValidator.handleInput(context.player(), payload));

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                sender.sendPacket(new ArcadeProfilesPayload(ArcadeProfiles.serverValues())));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                var payload = new ArcadeProfilesPayload(ArcadeProfiles.serverValues());
                PlayerLookup.all(server).forEach(player -> ServerPlayNetworking.send(player, payload));
            }
        });
    }
}
