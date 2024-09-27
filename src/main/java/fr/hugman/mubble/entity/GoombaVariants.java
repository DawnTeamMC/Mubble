package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class GoombaVariants {
    public static final RegistryKey<GoombaVariant> NORMAL = of("normal");

    private static RegistryKey<GoombaVariant> of(String path) {
        return of(Mubble.id(path));
    }

    public static RegistryKey<GoombaVariant> of(Identifier id) {
        return RegistryKey.of(MubbleRegistryKeys.GOOMBA_VARIANT, id);
    }
}