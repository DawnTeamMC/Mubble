package fr.hugman.mubble.entity.damage;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class MubbleDamageTypes {
    // SUPER MARIO
    public static final ResourceKey<DamageType> STOMP = of("stomp");
    public static final ResourceKey<DamageType> KOOPA_SHELL = of("koopa_shell");
    public static final ResourceKey<DamageType> FIREBALL = of("fireball");
    public static final ResourceKey<DamageType> ICEBALL = of("iceball");

    private static ResourceKey<DamageType> of(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Mubble.id(path));
    }
}
