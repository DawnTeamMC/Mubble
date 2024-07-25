package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MubbleEntityTypeKeys {
    public static final RegistryKey<EntityType<?>> SHOOTER_INK_BULLET = of("shooter_ink_bullet");

    private static RegistryKey<EntityType<?>> of(String path) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Mubble.id(path));
    }
}
