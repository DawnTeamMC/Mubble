package fr.hugman.mubble.item.weapon;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.registry.RegistryKey;

public class SplatoonWeaponTypeKeys {
    public static final RegistryKey<SplatoonWeaponType<?>> AUTOMATIC_SHOOTER = of("automatic_shooter");

    private static RegistryKey<SplatoonWeaponType<?>> of(String path) {
        return RegistryKey.of(MubbleRegistryKeys.SPLATOON_WEAPON_TYPE, Mubble.id(path));
    }
}
