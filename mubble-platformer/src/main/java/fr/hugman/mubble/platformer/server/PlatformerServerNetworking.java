package fr.hugman.mubble.platformer.server;

import fr.hugman.mubble.platformer.network.PlatformerProfilesPayload;
import fr.hugman.mubble.platformer.PlatformerProfiles;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class PlatformerServerNetworking {
    private PlatformerServerNetworking() {
    }

    public static void register() {
        // the input has no receiver here: ServerboundCustomPayloadPacketMixin takes it before Fabric does, to keep it in order
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                sender.sendPacket(new PlatformerProfilesPayload(PlatformerProfiles.serverValues())));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                var payload = new PlatformerProfilesPayload(PlatformerProfiles.serverValues());
                PlayerLookup.all(server).forEach(player -> ServerPlayNetworking.send(player, payload));
            }
        });
    }
}
