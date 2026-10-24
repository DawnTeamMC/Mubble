package fr.hugman.mubble.splatoon.core.registries;

import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeapon;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeaponType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class SplatoonBuiltInRegistries {
    public static final Registry<SplatoonWeaponType<?>> SPLATOON_WEAPON_TYPE = register(SplatoonRegistries.SPLATOON_WEAPON_TYPE);

    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.create(key).buildAndRegister();
    }

    public static void register() {
        DynamicRegistries.registerSynced(SplatoonRegistries.SPLATOON_WEAPON, SplatoonWeapon.DIRECT_CODEC);
    }
}
