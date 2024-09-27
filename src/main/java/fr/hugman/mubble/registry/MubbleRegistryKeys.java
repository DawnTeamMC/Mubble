package fr.hugman.mubble.registry;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.GoombaVariant;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MubbleRegistryKeys {
    public static final RegistryKey<Registry<GoombaVariant>> GOOMBA_VARIANT = of("goomba_variant");

    public static <T> RegistryKey<Registry<T>> of(String path) {
        return RegistryKey.ofRegistry(Mubble.id(path));
    }
}
