package fr.hugman.mubble.world.voyage.environment;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.network.protocol.common.custom.ActiveEnvironmentPayload;
import fr.hugman.mubble.network.protocol.common.custom.EnvironmentProfileSyncPayload;
import fr.hugman.mubble.world.level.EnvironmentOverridable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.Level;

/**
 * Applies environment profiles to levels and tells the affected clients about it.
 *
 * <p>Server-side entry point for phase 1. A trial calls {@link #apply} on entry and {@link #clear}
 * on exit; everything else here is keeping already-connected clients honest.
 */
public final class EnvironmentController {
    /** Which profile each voyage level is currently showing, so a reload can re-send it. */
    private static final Map<Level, Applied> ACTIVE = new HashMap<>();

    private record Applied(Identifier profileId, EnvironmentProfile profile, EnvironmentAttributeMap overrides) {
    }

    private EnvironmentController() {
    }

    public static void register() {
        // /reload re-reads dynamic registries server-side, but nothing re-sends them to a client that
        // is already playing. Without this an edited profile keeps rendering with its old values
        // until the player reconnects.
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resources, success) -> {
            if (!success) {
                return;
            }
            resyncProfiles(server);
            reapplyActive(server);
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> ACTIVE.clear());
    }

    /**
     * Applies a profile to a level and pushes it to everyone in that level.
     *
     * @param overrides per-instance values resolved by the caller, layered on top of the profile
     */
    public static void apply(ServerLevel level, Identifier profileId, EnvironmentAttributeMap overrides) {
        EnvironmentProfile profile = lookup(level.getServer(), profileId);
        if (profile == null) {
            // Loud, and no fallback: a missing profile is an authoring bug, and quietly rendering a
            // default sky is how that bug reaches players.
            Mubble.LOGGER.error("No environment profile '{}' — trial in {} will render unmodified", profileId, level.dimension().identifier());
            return;
        }

        ACTIVE.put(level, new Applied(profileId, profile, overrides));
        applyServerSide(level, profile, overrides);

        ActiveEnvironmentPayload payload = new ActiveEnvironmentPayload(Optional.of(profileId), overrides);
        for (ServerPlayer player : level.players()) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    /** Drops the override from a level and tells everyone in it to go back to vanilla resolution. */
    public static void clear(ServerLevel level) {
        ACTIVE.remove(level);
        ((EnvironmentOverridable) level).setEnvironmentOverrides(List.of());

        for (ServerPlayer player : level.players()) {
            ServerPlayNetworking.send(player, ActiveEnvironmentPayload.clear());
        }
    }

    /**
     * Sends a player the environment of whatever level they are in.
     *
     * <p>Needed whenever a player arrives somewhere the client does not already know about: joining,
     * or being teleported into a trial that was already running.
     */
    public static void sendTo(ServerPlayer player) {
        Applied applied = ACTIVE.get(player.level());
        ServerPlayNetworking.send(player, applied == null
                ? ActiveEnvironmentPayload.clear()
                : new ActiveEnvironmentPayload(Optional.of(applied.profileId()), applied.overrides()));
    }

    private static void applyServerSide(ServerLevel level, EnvironmentProfile profile, EnvironmentAttributeMap overrides) {
        ((EnvironmentOverridable) level).setEnvironmentOverrides(List.of(profile.attributes(), overrides));

        // fixed_time and weather are not attributes and cannot be expressed as layers; see
        // docs/environment-profiles.md for why, and what they currently do.
        profile.weather().ifPresent(weather -> applyWeather(level, weather));
    }

    private static void applyWeather(ServerLevel level, WeatherState weather) {
        MinecraftServer server = level.getServer();
        switch (weather) {
            case CLEAR -> server.setWeatherParameters(6000, 0, false, false);
            case RAIN -> server.setWeatherParameters(0, 6000, true, false);
            case THUNDER -> server.setWeatherParameters(0, 6000, true, true);
        }
    }

    private static EnvironmentProfile lookup(MinecraftServer server, Identifier id) {
        return server.registryAccess().lookupOrThrow(MubbleRegistries.ENVIRONMENT_PROFILE).getValue(id);
    }

    private static void resyncProfiles(MinecraftServer server) {
        Registry<EnvironmentProfile> registry = server.registryAccess().lookupOrThrow(MubbleRegistries.ENVIRONMENT_PROFILE);

        Map<Identifier, EnvironmentProfile> profiles = new HashMap<>();
        registry.entrySet().forEach(entry -> profiles.put(entry.getKey().identifier(), entry.getValue()));

        EnvironmentProfileSyncPayload payload = new EnvironmentProfileSyncPayload(profiles);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(player, payload);
        }
        Mubble.LOGGER.debug("Re-sent {} environment profile(s) after reload", profiles.size());
    }

    /** Re-resolves every active profile against the freshly loaded registry. */
    private static void reapplyActive(MinecraftServer server) {
        Map<Level, Applied> snapshot = Map.copyOf(ACTIVE);
        snapshot.forEach((level, applied) -> {
            if (level instanceof ServerLevel serverLevel) {
                apply(serverLevel, applied.profileId(), applied.overrides());
            }
        });
    }
}
