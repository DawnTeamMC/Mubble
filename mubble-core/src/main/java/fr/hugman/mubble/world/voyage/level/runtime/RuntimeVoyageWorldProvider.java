package fr.hugman.mubble.world.voyage.level.runtime;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.voyage.level.TrialInstance;
import fr.hugman.mubble.world.voyage.level.VoyageWorldHandle;
import fr.hugman.mubble.world.voyage.level.VoyageWorldProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import net.minecraft.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.storage.DerivedLevelData;

/**
 * Creates a real {@link ServerLevel} per trial and deletes it afterwards.
 *
 * <p>This is the whole of the "runtime dimension" trick — everything else in the voyage code talks
 * to {@link VoyageWorldProvider}. See {@code docs/runtime-worlds.md} for why this works, what it
 * costs, and what the alternative (a fixed pool) would look like.
 *
 * <p>Three invariants hold this together:
 *
 * <ol>
 *     <li>Levels are only ever added to or removed from the server's level map on the server
 *     thread. The map is a plain {@link java.util.LinkedHashMap} and {@code tickChildren} iterates
 *     it live, so {@link fr.hugman.mubble.mixin.MinecraftServerMixin} snapshots that iteration.</li>
 *     <li>No {@code LevelStem} is registered. Nothing reads that registry after boot, and staying
 *     out of it means a runtime level can never be written into the save and resurrected.</li>
 *     <li>Every level's directory is deleted on close, and any left over from a crash is deleted at
 *     startup.</li>
 * </ol>
 */
public final class RuntimeVoyageWorldProvider implements VoyageWorldProvider {
    /** All runtime levels live under this path, so orphans are easy to find and delete. */
    public static final String LEVEL_PATH_PREFIX = "voyage/";

    private final MinecraftServer server;
    private final AtomicLong nextId = new AtomicLong();
    private final List<Handle> open = new ArrayList<>();

    public RuntimeVoyageWorldProvider(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public VoyageWorldHandle open(TrialInstance trial, long seed) {
        assertServerThread();

        ResourceKey<Level> dimension = this.freshDimensionKey();
        ServerLevel level = new ServerLevel(
                this.server,
                Util.backgroundExecutor(),
                this.server.storageSource,
                // Reads world-level state (name, difficulty, game time) from the overworld and
                // discards writes, which is exactly what vanilla gives the nether and the end.
                new DerivedLevelData(this.server.getWorldData(), this.server.getWorldData().overworldData()),
                dimension,
                this.createStem(),
                false,
                BiomeManager.obfuscateSeed(seed),
                List.of(),
                false
        );

        this.server.levels.put(dimension, level);
        this.server.getPlayerList().addWorldborderListener(level);

        Handle handle = new Handle(dimension, level);
        this.open.add(handle);
        Mubble.LOGGER.debug("Opened voyage level {} for trial {} (seed {})", dimension.identifier(), trial.id(), seed);
        return handle;
    }

    @Override
    public void close(VoyageWorldHandle handle) {
        assertServerThread();

        if (!(handle instanceof Handle runtimeHandle)) {
            throw new IllegalArgumentException("Handle was not opened by this provider: " + handle);
        }
        if (!runtimeHandle.open) {
            return;
        }

        ServerLevel level = runtimeHandle.level;
        ResourceKey<Level> dimension = runtimeHandle.dimension;

        // Nobody should still be here — the session is meant to pull players out first — but a
        // stranded player is far worse than a noisy log, so evacuate rather than trust the caller.
        List<ServerPlayer> stranded = List.copyOf(level.players());
        if (!stranded.isEmpty()) {
            Mubble.LOGGER.error("Closing voyage level {} with {} player(s) still inside; evacuating to spawn",
                    dimension.identifier(), stranded.size());
            for (ServerPlayer player : stranded) {
                player.teleportTo(this.server.overworld(),
                        player.getX(), player.getY(), player.getZ(), java.util.Set.of(), player.getYRot(), player.getXRot(), false);
            }
        }

        runtimeHandle.open = false;
        this.open.remove(runtimeHandle);
        this.server.levels.remove(dimension, level);

        try {
            level.close();
        } catch (IOException e) {
            Mubble.LOGGER.error("Failed to close voyage level {}", dimension.identifier(), e);
        }

        deleteRecursively(this.server.storageSource.getDimensionPath(dimension));
        Mubble.LOGGER.debug("Closed voyage level {}", dimension.identifier());
    }

    /** Closes every handle this provider still holds. Called on server shutdown. */
    public void closeAll() {
        for (VoyageWorldHandle handle : List.copyOf(this.open)) {
            this.close(handle);
        }
    }

    /**
     * Deletes runtime level directories left behind by a crash or a hard kill.
     *
     * <p>Without this the world folder grows by one dead dimension every time the server does not
     * shut down cleanly.
     */
    public void purgeOrphanedLevels() {
        Path root = this.server.storageSource
                .getDimensionPath(ResourceKey.create(Registries.DIMENSION, Mubble.id(LEVEL_PATH_PREFIX + "x")))
                .getParent();
        if (root == null || !Files.isDirectory(root)) {
            return;
        }

        try (var children = Files.list(root)) {
            List<Path> orphans = children.toList();
            for (Path orphan : orphans) {
                deleteRecursively(orphan);
            }
            if (!orphans.isEmpty()) {
                Mubble.LOGGER.info("Deleted {} orphaned voyage level(s) from a previous run", orphans.size());
            }
        } catch (IOException e) {
            Mubble.LOGGER.error("Failed to scan for orphaned voyage levels in {}", root, e);
        }
    }

    /**
     * A void level with no structures and no features.
     *
     * <p>The dimension type is reused rather than registered: dimension types reach the client as
     * registry references during configuration, so a type invented at runtime could not be named to
     * a client that is already connected. Reusing {@code minecraft:overworld} keeps the reference
     * resolvable. Trials differentiate themselves through environment profiles (phase 1), not
     * through owning a dimension type.
     */
    private LevelStem createStem() {
        Holder<DimensionType> type = this.server.registryAccess()
                .lookupOrThrow(Registries.DIMENSION_TYPE)
                .getOrThrow(BuiltinDimensionTypes.OVERWORLD);
        Holder<Biome> biome = this.server.registryAccess()
                .lookupOrThrow(Registries.BIOME)
                .getOrThrow(Biomes.THE_VOID);

        FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(Optional.of(HolderSet.direct()), biome, List.of());
        // No layers were added, so this only exists to mark the generator as void-generating.
        settings.updateLayers();

        return new LevelStem(type, new FlatLevelSource(settings));
    }

    private ResourceKey<Level> freshDimensionKey() {
        // A counter rather than a random id: voyage code paths must stay free of live randomness
        // (design doc §6.9), and orphans are purged at startup so the counter cannot collide with
        // a directory left over from a previous run.
        while (true) {
            Identifier id = Mubble.id(LEVEL_PATH_PREFIX + this.nextId.getAndIncrement());
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, id);
            if (this.server.getLevel(key) == null) {
                return key;
            }
        }
    }

    private void assertServerThread() {
        if (!this.server.isSameThread()) {
            throw new IllegalStateException("Voyage levels may only be opened or closed on the server thread");
        }
    }

    private static void deleteRecursively(Path path) {
        if (!Files.exists(path)) {
            return;
        }
        try (var walk = Files.walk(path)) {
            for (Path child : walk.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(child);
            }
        } catch (IOException e) {
            Mubble.LOGGER.error("Failed to delete voyage level directory {}", path, e);
        }
    }

    private static final class Handle implements VoyageWorldHandle {
        private final ResourceKey<Level> dimension;
        private final ServerLevel level;
        private boolean open = true;

        private Handle(ResourceKey<Level> dimension, ServerLevel level) {
            this.dimension = dimension;
            this.level = level;
        }

        @Override
        public ServerLevel level() {
            if (!this.open) {
                throw new IllegalStateException("Voyage level " + this.dimension.identifier() + " has been closed");
            }
            return this.level;
        }

        @Override
        public ResourceKey<Level> dimension() {
            return this.dimension;
        }

        @Override
        public boolean isOpen() {
            return this.open;
        }
    }
}
