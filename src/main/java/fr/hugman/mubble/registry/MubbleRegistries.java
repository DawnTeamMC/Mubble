package fr.hugman.mubble.registry;

import fr.hugman.mubble.item.weapon.SplatoonWeapon;
import fr.hugman.mubble.item.weapon.SplatoonWeaponType;
import fr.hugman.mubble.entity.GoombaVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MubbleRegistries {
    public static final Registry<SplatoonWeaponType<?>> SPLATOON_WEAPON_TYPE = of(MubbleRegistryKeys.SPLATOON_WEAPON_TYPE);

    private static <T> Registry<T> of(RegistryKey<Registry<T>> key) {
        return FabricRegistryBuilder.createSimple(key).buildAndRegister();
    }

    public static void register() {
        DynamicRegistries.registerSynced(MubbleRegistryKeys.GOOMBA_VARIANT, GoombaVariant.CODEC);
        DynamicRegistries.registerSynced(MubbleRegistryKeys.SPLATOON_WEAPON, SplatoonWeapon.TYPE_CODEC);
    }
}
