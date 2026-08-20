package fr.hugman.mubble.client.network;

import fr.hugman.mubble.network.protocol.common.custom.EnvironmentProfileSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/** Takes the post-{@code /reload} profile registry and replaces whatever the client had. */
public class EnvironmentProfileSyncPayloadReceiver implements ClientPlayNetworking.PlayPayloadHandler<EnvironmentProfileSyncPayload> {
    public static final EnvironmentProfileSyncPayloadReceiver INSTANCE = new EnvironmentProfileSyncPayloadReceiver();

    @Override
    public void receive(EnvironmentProfileSyncPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> ClientEnvironmentProfiles.acceptReload(payload.profiles()));
    }
}
