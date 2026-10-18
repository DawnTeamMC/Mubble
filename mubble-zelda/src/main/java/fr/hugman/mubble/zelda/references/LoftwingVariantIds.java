package fr.hugman.mubble.zelda.references;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingVariant;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class LoftwingVariantIds {
    public static final ResourceKey<LoftwingVariant> BLUE = createKey("blue");
    public static final ResourceKey<LoftwingVariant> GREEN = createKey("green");
    public static final ResourceKey<LoftwingVariant> YELLOW = createKey("yellow");
    public static final ResourceKey<LoftwingVariant> VIOLET = createKey("violet");
    /** Link's own breed, thought extinct by the people of Skyloft: by far the rarest. */
    public static final ResourceKey<LoftwingVariant> CRIMSON = createKey("crimson");

    private static ResourceKey<LoftwingVariant> createKey(String path) {
        return createKey(Zelda.id(path));
    }

    public static ResourceKey<LoftwingVariant> createKey(Identifier id) {
        return ResourceKey.create(ZeldaRegistries.LOFTWING_VARIANT, id);
    }
}
