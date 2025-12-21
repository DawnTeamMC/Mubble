package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class MubbleEntityTypeKeys {
    // SUPER MARIO
    public static final ResourceKey<EntityType<?>> GOOMBA = of("goomba");
    public static final ResourceKey<EntityType<?>> GREEN_KOOPA_SHELL = of("green_koopa_shell");
    public static final ResourceKey<EntityType<?>> RED_KOOPA_SHELL = of("red_koopa_shell");
    public static final ResourceKey<EntityType<?>> FIREBALL = of("fireball");
    public static final ResourceKey<EntityType<?>> ICEBALL = of("iceball");

    private static ResourceKey<EntityType<?>> of(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Mubble.id(path));
    }
}