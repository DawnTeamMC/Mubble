package fr.hugman.mubble.zelda.references;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class ZeldaEntityTypeIds {
    public static final ResourceKey<EntityType<?>> LOFTWING = createKey("loftwing");

    private static ResourceKey<EntityType<?>> createKey(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Zelda.id(path));
    }
}
