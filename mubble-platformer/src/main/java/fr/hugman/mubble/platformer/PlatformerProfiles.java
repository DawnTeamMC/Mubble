package fr.hugman.mubble.platformer;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;

/**
 * The values profiles currently have.
 * <p>
 * Profiles are a dynamic registry, which the game only reads once per world: a {@code /reload} would
 * not reach them. So on top of the registry, every data reload reads the profile files again into a
 * table of live values, which the server pushes to its clients and both sides look profiles up in
 * first. Editing the numbers of a profile and reloading changes the movement right away; adding a
 * new profile still takes a restart, since the registry is what knows which profiles exist.
 */
public final class PlatformerProfiles {
    private static final Map<ResourceKey<PlatformerProfile>, PlatformerProfile> SERVER = new ConcurrentHashMap<>();
    private static final Map<ResourceKey<PlatformerProfile>, PlatformerProfile> CLIENT = new ConcurrentHashMap<>();

    private PlatformerProfiles() {
    }

    public static Optional<PlatformerProfile> get(Level level, ResourceKey<PlatformerProfile> key) {
        var live = (level.isClientSide() ? CLIENT : SERVER).get(key);
        if (live != null) {
            return Optional.of(live);
        }
        return level.registryAccess().lookup(PlatformerRegistries.PLATFORMER_PROFILE).flatMap(registry -> registry.getOptional(key));
    }

    /** The live values of the server, which it sends to its clients. */
    public static Map<ResourceKey<PlatformerProfile>, PlatformerProfile> serverValues() {
        return Map.copyOf(SERVER);
    }

    public static void setServerValues(Map<ResourceKey<PlatformerProfile>, PlatformerProfile> values) {
        SERVER.clear();
        SERVER.putAll(values);
    }

    public static void setClientValues(Map<ResourceKey<PlatformerProfile>, PlatformerProfile> values) {
        CLIENT.clear();
        CLIENT.putAll(values);
    }

    public static void clearClientValues() {
        CLIENT.clear();
    }

    /** Reads the profile files again on every data reload. */
    public static final class ReloadListener extends SimpleJsonResourceReloadListener<PlatformerProfile> {
        public ReloadListener(HolderLookup.Provider registries) {
            super(registries, PlatformerProfile.DIRECT_CODEC, PlatformerRegistries.PLATFORMER_PROFILE);
        }

        @Override
        protected void apply(Map<Identifier, PlatformerProfile> profiles, ResourceManager manager, ProfilerFiller profiler) {
            var values = new java.util.HashMap<ResourceKey<PlatformerProfile>, PlatformerProfile>();
            profiles.forEach((id, profile) -> values.put(ResourceKey.create(PlatformerRegistries.PLATFORMER_PROFILE, id), profile));
            setServerValues(values);
        }
    }
}
