package fr.hugman.mubble.entity.damage;

import fr.hugman.mubble.Mubble;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MubbleDamageTypeKeys {
    // SUPER MARIO
    public static final RegistryKey<DamageType> KOOPA_SHELL = of("koopa_shell");

    private static RegistryKey<DamageType> of(String path) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Mubble.id(path));
    }

}
