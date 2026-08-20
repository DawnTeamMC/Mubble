package fr.hugman.mubble.world.voyage.level.runtime;

import fr.hugman.mubble.world.voyage.level.VoyageWorldProvider;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

/**
 * Owns the one {@link VoyageWorldProvider} a running server has.
 *
 * <p>Phase 0 scaffolding: once the session runtime exists (phase 3) the provider belongs to it, and
 * this class goes away.
 */
public final class VoyageWorlds {
    private static RuntimeVoyageWorldProvider current;

    private VoyageWorlds() {
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            RuntimeVoyageWorldProvider provider = new RuntimeVoyageWorldProvider(server);
            provider.purgeOrphanedLevels();
            current = provider;
        });

        // Runs before levels are saved and closed, so voyage levels are gone by the time vanilla
        // walks the level map.
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (current != null) {
                current.closeAll();
                current = null;
            }
        });
    }

    /**
     * {@return the provider for the running server}
     *
     * @throws IllegalStateException if no server is running
     */
    public static VoyageWorldProvider get(MinecraftServer server) {
        RuntimeVoyageWorldProvider provider = current;
        if (provider == null) {
            throw new IllegalStateException("No voyage world provider; the server is not running");
        }
        return provider;
    }
}
