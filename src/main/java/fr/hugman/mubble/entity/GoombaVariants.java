package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class GoombaVariants {
    public static final ResourceKey<GoombaVariant> NORMAL = of("normal");
    public static final ResourceKey<GoombaVariant> MINI = of("mini");

    private static ResourceKey<GoombaVariant> of(String path) {
        return of(Mubble.id(path));
    }

    public static ResourceKey<GoombaVariant> of(Identifier id) {
        return ResourceKey.create(MubbleRegistryKeys.GOOMBA_VARIANT, id);
    }
}