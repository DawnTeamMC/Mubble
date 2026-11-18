package fr.hugman.mubble.platformer.references;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.PlatformerProfile;
import net.minecraft.resources.ResourceKey;

public class PlatformerProfileIds {
    /** The platformer movement of the normal game, a little softer and charging food for it. */
    public static final ResourceKey<PlatformerProfile> OVERWORLD = createKey("overworld");
    /** The platformer movement of the trials, at full strength. */
    public static final ResourceKey<PlatformerProfile> TRIAL = createKey("trial");

    private static ResourceKey<PlatformerProfile> createKey(String path) {
        return ResourceKey.create(PlatformerRegistries.PLATFORMER_PROFILE, Mubble.id(path));
    }
}
