package fr.hugman.mubble.fortnite.references;

import fr.hugman.mubble.fortnite.Fortnite;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class FortniteEntityTypeIds {
    public static final ResourceKey<EntityType<?>> IMPULSE_GRENADE = createKey("impulse_grenade");

    private static ResourceKey<EntityType<?>> createKey(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Fortnite.id(path));
    }
}
