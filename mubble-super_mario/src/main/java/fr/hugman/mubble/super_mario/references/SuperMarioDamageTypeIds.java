package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class SuperMarioDamageTypeIds {
    public static final ResourceKey<DamageType> STOMP = createKey("stomp");
    public static final ResourceKey<DamageType> KOOPA_SHELL = createKey("koopa_shell");
    public static final ResourceKey<DamageType> FIREBALL = createKey("fireball");
    public static final ResourceKey<DamageType> ICEBALL = createKey("iceball");
    public static final ResourceKey<DamageType> GOLD_FIREBALL = createKey("gold_fireball");
    public static final ResourceKey<DamageType> FLOWER = createKey("flower");

    private static ResourceKey<DamageType> createKey(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, SuperMario.id(path));
    }
}
