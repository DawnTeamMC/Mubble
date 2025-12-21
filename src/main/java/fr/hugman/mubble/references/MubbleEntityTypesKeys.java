package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class MubbleEntityTypesKeys {
    // SUPER MARIO
    public static final ResourceKey<EntityType<?>> GOOMBA = createKey("goomba");
    public static final ResourceKey<EntityType<?>> GREEN_KOOPA_SHELL = createKey("green_koopa_shell");
    public static final ResourceKey<EntityType<?>> RED_KOOPA_SHELL = createKey("red_koopa_shell");
    public static final ResourceKey<EntityType<?>> FIREBALL = createKey("fireball");
    public static final ResourceKey<EntityType<?>> ICEBALL = createKey("iceball");

    private static ResourceKey<EntityType<?>> createKey(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Mubble.id(path));
    }
}