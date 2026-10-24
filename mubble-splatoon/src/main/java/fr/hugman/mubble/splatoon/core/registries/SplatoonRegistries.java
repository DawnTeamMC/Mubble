package fr.hugman.mubble.splatoon.core.registries;

import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeapon;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeaponType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class SplatoonRegistries {
    public static final ResourceKey<Registry<SplatoonWeapon>> SPLATOON_WEAPON = createRegistryKey("splatoon_weapon");
    public static final ResourceKey<Registry<SplatoonWeaponType<?>>> SPLATOON_WEAPON_TYPE = createRegistryKey("splatoon_weapon_type");

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(Splatoon.id(path));
    }
}
