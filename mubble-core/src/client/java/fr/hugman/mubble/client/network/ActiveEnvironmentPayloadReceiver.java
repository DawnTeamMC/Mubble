package fr.hugman.mubble.client.network;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.network.protocol.common.custom.ActiveEnvironmentPayload;
import fr.hugman.mubble.world.level.EnvironmentOverridable;
import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;

/**
 * Applies whatever environment the server says is active.
 *
 * <p>Applies, never recomputes: the per-instance overrides arrive already resolved, so this never
 * sees a seed or a list of candidate values.
 */
public class ActiveEnvironmentPayloadReceiver implements ClientPlayNetworking.PlayPayloadHandler<ActiveEnvironmentPayload> {
    public static final ActiveEnvironmentPayloadReceiver INSTANCE = new ActiveEnvironmentPayloadReceiver();

    @Override
    public void receive(ActiveEnvironmentPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            ClientLevel level = context.client().level;
            if (level == null) {
                return;
            }

            if (payload.profile().isEmpty()) {
                ((EnvironmentOverridable) level).setEnvironmentOverrides(List.of());
                return;
            }

            Identifier id = payload.profile().get();
            EnvironmentProfile profile = ClientEnvironmentProfiles.get(id);
            if (profile == null) {
                // Being told to apply a profile we were never sent is a sync bug, not a content bug.
                // Say so, and leave the sky alone rather than inventing one.
                Mubble.LOGGER.error(
                        "Server asked for environment profile '{}', which this client was never sent. "
                                + "This is a registry sync bug — the trial will render unmodified.", id);
                return;
            }

            // fixed(), not resolve(): a synced profile has no candidate lists left in it, and asking
            // for a resolution here would need a seed the client is deliberately never given.
            ((EnvironmentOverridable) level).setEnvironmentOverrides(List.of(profile.attributes().fixed(), payload.overrides()));
        });
    }
}
