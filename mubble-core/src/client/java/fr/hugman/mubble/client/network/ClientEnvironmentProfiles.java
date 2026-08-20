package fr.hugman.mubble.client.network;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

/**
 * The client's view of the environment profile registry.
 *
 * <p>Profiles normally arrive with the other datapack registries during configuration. Anything
 * re-sent after a {@code /reload} lands here instead and wins, because the synced registry is
 * immutable once the connection is playing.
 */
public final class ClientEnvironmentProfiles {
    private static final Map<Identifier, EnvironmentProfile> RELOADED = new HashMap<>();

    private ClientEnvironmentProfiles() {
    }

    public static void acceptReload(Map<Identifier, EnvironmentProfile> profiles) {
        RELOADED.clear();
        RELOADED.putAll(profiles);
    }

    public static void clear() {
        RELOADED.clear();
    }

    /** {@return the profile with this id, or {@code null} if this client was never sent one} */
    public static @Nullable EnvironmentProfile get(Identifier id) {
        EnvironmentProfile reloaded = RELOADED.get(id);
        if (reloaded != null) {
            return reloaded;
        }

        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            return null;
        }
        return connection.registryAccess().lookupOrThrow(MubbleRegistries.ENVIRONMENT_PROFILE).getValue(id);
    }
}
