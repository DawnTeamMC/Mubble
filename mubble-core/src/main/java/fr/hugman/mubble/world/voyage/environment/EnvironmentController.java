package fr.hugman.mubble.world.voyage.environment;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.network.protocol.common.custom.ActiveEnvironmentPayload;
import fr.hugman.mubble.network.protocol.common.custom.EnvironmentProfileSyncPayload;
import fr.hugman.mubble.world.level.EnvironmentOverridable;
import fr.hugman.mubble.world.level.WeatherOverridable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.WeatherData;

/**
 * Applies environment profiles to levels and tells the affected clients about it.
 *
 * <p>Server-side entry point for phase 1. A trial calls {@link #apply} on entry and {@link #clear}
 * on exit; everything else here is keeping already-connected clients honest.
 */
public final class EnvironmentController {
    /**
     * Which profile each voyage level is currently showing, so a reload can re-send it.
     *
     * <p>Weak keys. Whoever closes a trial level is expected to {@link #clear} it first, but a
     * voyage level is deleted outright rather than merely emptied, and one forgotten call would
     * otherwise pin a whole deleted level and its chunks in memory for as long as the server runs.
     */
    private static final Map<Level, Applied> ACTIVE = new WeakHashMap<>();

    /**
     * The seed is kept, not the map it resolved to. {@code /reload} replaces the profile object, and
     * re-resolving the new one against the same node seed is what makes an edited candidate list
     * take effect without the trial silently changing which candidate it had picked.
     */
    private record Applied(Identifier profileId, long nodeSeed, EnvironmentAttributeMap overrides) {
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
     * @param nodeSeed  the seed any candidate lists in the profile resolve against
     * @param overrides per-instance values resolved by the caller, layered on top of the profile
     * @return whether the profile existed; {@code false} leaves the level untouched
     */
    public static boolean apply(ServerLevel level, Identifier profileId, long nodeSeed, EnvironmentAttributeMap overrides) {
        EnvironmentProfile profile = lookup(level.getServer(), profileId);
        if (profile == null) {
            // Loud, and no fallback: a missing profile is an authoring bug, and quietly rendering a
            // default sky is how that bug reaches players.
            Mubble.LOGGER.error("No environment profile '{}' — trial in {} will render unmodified. {}",
                    profileId, level.dimension().identifier(), describeLoaded(level.getServer()));
            return false;
        }

        ACTIVE.put(level, new Applied(profileId, nodeSeed, overrides));

        // Resolved once, here, and then used for both sides. The client is sent the same two layers
        // the server stacks, so a mistake shows up as the wrong sky rather than as a desync where
        // only one of them is wrong.
        EnvironmentAttributeMap resolved = resolveOverrides(profile, nodeSeed, overrides);
        applyServerSide(level, profile, resolved);

        ActiveEnvironmentPayload payload = new ActiveEnvironmentPayload(Optional.of(profileId), resolved);
        for (ServerPlayer player : level.players()) {
            ServerPlayNetworking.send(player, payload);
        }
        return true;
    }

    /**
     * {@return the layer that goes on top of the profile}: whatever its candidate lists resolved to,
     * with the caller's per-instance overrides winning over both.
     */
    private static EnvironmentAttributeMap resolveOverrides(EnvironmentProfile profile, long nodeSeed, EnvironmentAttributeMap overrides) {
        return EnvironmentAttributeMap.builder()
                .putAll(profile.attributes().resolveCandidates(nodeSeed))
                .putAll(overrides)
                .build();
    }

    /** {@return how many profiles are loaded, and which} — the useful half of a "profile not found" */
    public static String describeLoaded(MinecraftServer server) {
        List<Identifier> ids = List.copyOf(server.registryAccess()
                .lookupOrThrow(MubbleRegistries.ENVIRONMENT_PROFILE).keySet());
        if (ids.isEmpty()) {
            return "No environment profiles are loaded at all, so no data pack defining them is active.";
        }
        return ids.size() + " loaded: " + ids;
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
        if (applied == null) {
            ServerPlayNetworking.send(player, ActiveEnvironmentPayload.clear());
            return;
        }

        EnvironmentProfile profile = lookup(player.level().getServer(), applied.profileId());
        if (profile == null) {
            // The profile was applied and has since gone away, which only happens if a reload dropped
            // it. Better an unmodified sky than a payload naming something the client cannot resolve.
            Mubble.LOGGER.error("Environment profile '{}' is active in {} but no longer loaded",
                    applied.profileId(), player.level().dimension().identifier());
            ServerPlayNetworking.send(player, ActiveEnvironmentPayload.clear());
            return;
        }

        ServerPlayNetworking.send(player, new ActiveEnvironmentPayload(Optional.of(applied.profileId()),
                resolveOverrides(profile, applied.nodeSeed(), applied.overrides())));
    }

    private static void applyServerSide(ServerLevel level, EnvironmentProfile profile, EnvironmentAttributeMap resolved) {
        ((EnvironmentOverridable) level).setEnvironmentOverrides(List.of(profile.attributes().fixed(), resolved));

        // fixed_time is not applied here. It is a clock, and a clock can only be given to a level
        // when the level is created, so the trial's level provider reads it off the definition; see
        // TrialDefinition#fixedTime. Nothing sets it for a level that already exists, so applying a
        // profile to a running level cannot change the time.
        //
        // weather is not an attribute either. It is a level's own since we gave trial levels their
        // own WeatherData; see WeatherOverridable.
        profile.weather().ifPresent(weather -> applyWeather(level, weather));
    }

    /**
     * Sets the weather of one level, and only that level.
     *
     * <p>Deliberately not {@code MinecraftServer#setWeatherParameters}, which is what this used to
     * call: that writes the server's shared weather, so a stormy trial rained on everyone. A level
     * that has been given its own {@link WeatherData} can be changed on its own.
     */
    private static void applyWeather(ServerLevel level, WeatherState weather) {
        WeatherData data = ((WeatherOverridable) level).getOwnWeather();
        if (data == null) {
            // Refused rather than applied to the server's weather. A profile is allowed to be used
            // anywhere, but "make it storm here" must never turn into "make it storm everywhere",
            // and silently doing nothing would leave someone hunting for why.
            Mubble.LOGGER.warn("Ignoring the '{}' weather of an environment profile: {} shares the server's weather, "
                            + "and changing it would change everyone's. Only trial levels have their own.",
                    weather.getSerializedName(), level.dimension().identifier());
            return;
        }

        boolean raining = weather != WeatherState.CLEAR;
        boolean thundering = weather == WeatherState.THUNDER;

        data.setRaining(raining);
        data.setThundering(thundering);
        // The timers are dead state while a trial level has ADVANCE_WEATHER off, which is how the
        // weather is held still. Zeroing them means that turning the gamerule back on picks fresh
        // durations rather than flipping the weather on the next tick.
        data.setClearWeatherTime(0);
        data.setRainTime(0);
        data.setThunderTime(0);

        // Snap rather than let the cycle ramp over five seconds — a trial should look like itself on
        // arrival. The ramp is also what normally broadcasts the change, so snapping means telling
        // whoever is already here by hand. Players arriving later are covered by
        // PlayerList#sendLevelInfo, which reads these levels on a dimension change.
        level.setRainLevel(raining ? 1.0F : 0.0F);
        level.setThunderLevel(thundering ? 1.0F : 0.0F);
        for (ServerPlayer player : level.players()) {
            player.connection.send(new ClientboundGameEventPacket(
                    raining ? ClientboundGameEventPacket.START_RAINING : ClientboundGameEventPacket.STOP_RAINING, 0.0F));
            player.connection.send(new ClientboundGameEventPacket(
                    ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, level.getRainLevel(1.0F)));
            player.connection.send(new ClientboundGameEventPacket(
                    ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, level.getThunderLevel(1.0F)));
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
                apply(serverLevel, applied.profileId(), applied.nodeSeed(), applied.overrides());
            }
        });
    }
}
