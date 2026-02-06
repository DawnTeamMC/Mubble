package fr.hugman.mubble.splatoon.references;

import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.core.registries.SplatoonRegistries;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeaponType;
import net.minecraft.resources.ResourceKey;

public class SplatoonWeaponTypeKeys {
    public static final ResourceKey<SplatoonWeaponType<?>> AUTOMATIC_SHOOTER = createKey("automatic_shooter");

    private static ResourceKey<SplatoonWeaponType<?>> createKey(String path) {
        return ResourceKey.create(SplatoonRegistries.SPLATOON_WEAPON_TYPE, Splatoon.id(path));
    }
}
