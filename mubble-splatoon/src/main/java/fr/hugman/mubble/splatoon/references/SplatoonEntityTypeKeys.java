package fr.hugman.mubble.splatoon.references;

import fr.hugman.mubble.splatoon.Splatoon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class SplatoonEntityTypeKeys {
    public static final ResourceKey<EntityType<?>> SHOOTER_INK_BULLET = createKey("shooter_ink_bullet");

    private static ResourceKey<EntityType<?>> createKey(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Splatoon.id(path));
    }
}