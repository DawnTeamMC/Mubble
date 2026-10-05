package fr.hugman.mubble.arcade.references;

import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.ArcadeProfile;
import net.minecraft.resources.ResourceKey;

public class ArcadeProfileIds {
    /** The arcade movement of the normal game, a little softer and charging food for it. */
    public static final ResourceKey<ArcadeProfile> OVERWORLD = createKey("overworld");
    /** The arcade movement of the trials, at full strength. */
    public static final ResourceKey<ArcadeProfile> TRIAL = createKey("trial");

    private static ResourceKey<ArcadeProfile> createKey(String path) {
        return ResourceKey.create(ArcadeRegistries.ARCADE_PROFILE, Mubble.id(path));
    }
}
