package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class MubbleEntityTypeKeys {
    public static final ResourceKey<EntityType<?>> COLLECTIBLE = createKey("collectible");

    private static ResourceKey<EntityType<?>> createKey(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Mubble.id(path));
    }
}