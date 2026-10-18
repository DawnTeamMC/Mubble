package fr.hugman.mubble.zelda.core.registries;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ZeldaRegistries {
    public static final ResourceKey<Registry<LoftwingVariant>> LOFTWING_VARIANT = createRegistryKey("loftwing_variant");

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(Zelda.id(path));
    }
}
