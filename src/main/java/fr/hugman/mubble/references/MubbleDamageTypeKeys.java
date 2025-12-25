package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class MubbleDamageTypeKeys {
    // SUPER MARIO
    public static final ResourceKey<DamageType> STOMP = createKey("stomp");
    public static final ResourceKey<DamageType> KOOPA_SHELL = createKey("koopa_shell");
    public static final ResourceKey<DamageType> FIREBALL = createKey("fireball");
    public static final ResourceKey<DamageType> ICEBALL = createKey("iceball");

    private static ResourceKey<DamageType> createKey(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Mubble.id(path));
    }
}
