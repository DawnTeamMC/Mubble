package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MubbleEntityTypeKeys {
    // SUPER MARIO
    public static final RegistryKey<EntityType<?>> GOOMBA = of("goomba");
    public static final RegistryKey<EntityType<?>> FIREBALL = of("fireball");

    private static RegistryKey<EntityType<?>> of(String path) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Mubble.id(path));
    }
}