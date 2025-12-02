package fr.hugman.mubble.entity.damage;

import fr.hugman.mubble.Mubble;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MubbleDamageTypes {
    // SUPER MARIO
    public static final RegistryKey<DamageType> STOMP = of("stomp");
    public static final RegistryKey<DamageType> KOOPA_SHELL = of("koopa_shell");
    public static final RegistryKey<DamageType> FIREBALL = of("fireball");
    public static final RegistryKey<DamageType> ICEBALL = of("iceball");

    private static RegistryKey<DamageType> of(String path) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Mubble.id(path));
    }
}
