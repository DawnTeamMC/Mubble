package fr.hugman.mubble.registry;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.item.weapon.SplatoonWeapon;
import fr.hugman.mubble.item.weapon.SplatoonWeaponType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MubbleRegistryKeys {
    public static final RegistryKey<Registry<GoombaVariant>> GOOMBA_VARIANT = of("goomba_variant");

    public static final RegistryKey<Registry<SplatoonWeapon>> SPLATOON_WEAPON = of("splatoon_weapon");
    public static final RegistryKey<Registry<SplatoonWeaponType<?>>> SPLATOON_WEAPON_TYPE = of("splatoon_weapon_type");

    public static <T> RegistryKey<Registry<T>> of(String path) {
        return RegistryKey.ofRegistry(Mubble.id(path));
    }
}
