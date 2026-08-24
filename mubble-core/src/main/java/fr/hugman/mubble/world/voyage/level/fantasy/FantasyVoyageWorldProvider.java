package fr.hugman.mubble.world.voyage.level.fantasy;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.voyage.NodeInstance;
import fr.hugman.mubble.world.voyage.level.VoyageWorldHandle;
import fr.hugman.mubble.world.level.WeatherOverridable;
import fr.hugman.mubble.world.voyage.level.VoyageWorldProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.saveddata.WeatherData;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeLevelConfig;
import xyz.nucleoid.fantasy.RuntimeLevelHandle;
import xyz.nucleoid.fantasy.util.VoidChunkGenerator;

/**
 * Opens voyage levels through <a href="https://github.com/NucleoidMC/fantasy">Fantasy</a>.
 *
 * <p>Fantasy owns the whole runtime-dimension lifecycle: creating the level, registering it with the
 * server, keeping the tick loop safe against a level appearing or vanishing mid-tick, and deleting
 * the directory on close. See {@code design/voyages_poc/implementation.md} for why we are not doing
 * it ourselves.
 *
 * <p>This class is the only place in the mod that knows Fantasy exists. Everything else talks to
 * {@link VoyageWorldProvider}.
 */
public final class FantasyVoyageWorldProvider implements VoyageWorldProvider {
    /** All voyage levels live under this path, so they are recognisable in logs and on disk. */
    public static final String LEVEL_PATH_PREFIX = "voyage/";

    private final MinecraftServer server;
    private final AtomicLong nextId = new AtomicLong();
    private final List<Handle> open = new ArrayList<>();

    public FantasyVoyageWorldProvider(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public VoyageWorldHandle open(NodeInstance node) {
        this.assertServerThread();

        RuntimeLevelConfig config = new RuntimeLevelConfig()
                // Dimension types reach the client as registry references during configuration, so a
                // type invented at runtime could not be named to a connected client. Reuse the
                // overworld's; trials differentiate themselves through environment profiles.
                .setDimensionType(BuiltinDimensionTypes.OVERWORLD)
                .setGenerator(new VoidChunkGenerator(this.server, Biomes.THE_VOID))
                .setSeed(node.nodeSeed())
                .setShouldTickTime(false)
                // A trial's weather is whatever its environment profile says and nothing else. The
                // level owns its weather (see below), so leaving the cycle running would have it
                // drift off on its own timers halfway through a trial.
                .setGameRule(GameRules.ADVANCE_WEATHER, false);

        // The one thing an environment profile cannot express as a layer. Vanilla's clock manager
        // belongs to the server, so /time and anything built on it moves every level at once; Fantasy
        // gives a runtime level its own, but only at creation. Paused as well as set, so a trial that
        // asks for dusk stays at dusk. WorldClocks.OVERWORLD is the default clock of the dimension
        // type chosen above — the two have to agree.
        node.content().fixedTime().ifPresent(time -> config.setClockTime(WorldClocks.OVERWORLD, time, true));

        RuntimeLevelHandle fantasyHandle = Fantasy.get(this.server).openTemporaryLevel(this.freshLevelId(), config);
        this.isolateWeather(fantasyHandle.asLevel());

        Handle handle = new Handle(fantasyHandle);
        this.open.add(handle);
        Mubble.LOGGER.debug("Opened voyage level {} for {} at node '{}' (seed {})",
                handle.dimension().identifier(), node.id(), node.nodePath(), node.nodeSeed());
        return handle;
    }

    @Override
    public void close(VoyageWorldHandle handle) {
        this.assertServerThread();

        if (!(handle instanceof Handle voyageHandle)) {
            throw new IllegalArgumentException("Handle was not opened by this provider: " + handle);
        }
        if (!voyageHandle.open) {
            return;
        }

        ServerLevel level = voyageHandle.fantasyHandle.asLevel();
        Identifier id = voyageHandle.dimension().identifier();

        // Nobody should still be here — the session is meant to pull players out first — but a
        // stranded player is far worse than a noisy log, so evacuate rather than trust the caller.
        List<ServerPlayer> stranded = List.copyOf(level.players());
        if (!stranded.isEmpty()) {
            Mubble.LOGGER.error("Closing voyage level {} with {} player(s) still inside; evacuating to spawn", id, stranded.size());

            // World spawn, not the coordinates they happen to be standing on. A trial platform sits
            // at whatever position suited the trial, and copying that into the overworld drops people
            // inside terrain or in mid-air — which is how a player ends up somewhere baffling after a
            // shutdown mid-trial. Restoring the *right* position is the session's job, not ours; all
            // this owes anyone is somewhere survivable.
            ServerLevel overworld = this.server.overworld();
            BlockPos spawn = this.server.getRespawnData().pos();
            for (ServerPlayer player : stranded) {
                player.teleportTo(overworld, spawn.getX() + 0.5D, spawn.getY(), spawn.getZ() + 0.5D,
                        Set.of(), player.getYRot(), player.getXRot(), false);
            }
        }

        voyageHandle.open = false;
        this.open.remove(voyageHandle);
        voyageHandle.fantasyHandle.delete();
        Mubble.LOGGER.debug("Closed voyage level {}", id);
    }

    /**
     * Gives a fresh level its own weather, so a trial is sealed off from the rest of the server.
     *
     * <p>Weather is one object on the server in 26.2, shared by every level, so without this a
     * thunderstorm in a trial rains on someone's overworld build and {@code /weather clear} outside
     * cancels the storm a trial asked for. A new {@link WeatherData} is clear.
     *
     * <p>The rain and thunder levels are zeroed as well. They are already per level, but the
     * constructor primed them from the <em>server's</em> weather, so a level created during a storm
     * would open mid-downpour and then spend a hundred ticks fading out.
     */
    private void isolateWeather(ServerLevel level) {
        ((WeatherOverridable) level).setOwnWeather(new WeatherData());
        level.setRainLevel(0.0F);
        level.setThunderLevel(0.0F);
    }

    /** Closes every handle this provider still holds. Called on server shutdown. */
    public void closeAll() {
        for (VoyageWorldHandle handle : List.copyOf(this.open)) {
            this.close(handle);
        }
    }

    private Identifier freshLevelId() {
        // A counter rather than a random id: voyage code paths must stay free of live randomness
        // (design doc §6.9). Fantasy deletes temporary levels on close and on startup, so the
        // counter cannot collide with a directory left over from a previous run.
        while (true) {
            Identifier id = Mubble.id(LEVEL_PATH_PREFIX + this.nextId.getAndIncrement());
            if (this.server.getLevel(ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, id)) == null) {
                return id;
            }
        }
    }

    private void assertServerThread() {
        if (!this.server.isSameThread()) {
            throw new IllegalStateException("Voyage levels may only be opened or closed on the server thread");
        }
    }

    private static final class Handle implements VoyageWorldHandle {
        private final RuntimeLevelHandle fantasyHandle;
        private boolean open = true;

        private Handle(RuntimeLevelHandle fantasyHandle) {
            this.fantasyHandle = fantasyHandle;
        }

        @Override
        public ServerLevel level() {
            if (!this.open) {
                throw new IllegalStateException("Voyage level " + this.dimension().identifier() + " has been closed");
            }
            return this.fantasyHandle.asLevel();
        }

        @Override
        public ResourceKey<Level> dimension() {
            return this.fantasyHandle.getRegistryKey();
        }

        @Override
        public boolean isOpen() {
            return this.open;
        }
    }
}
