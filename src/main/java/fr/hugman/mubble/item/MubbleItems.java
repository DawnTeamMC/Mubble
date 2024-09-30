package fr.hugman.mubble.item;

import fr.hugman.mubble.component.MubbleDataComponentsTypes;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.entity.projectile.ShooterInkBulletConfig;
import fr.hugman.mubble.item.weapon.AutomaticShooterConfig;
import fr.hugman.mubble.item.weapon.SplatoonWeapon;
import fr.hugman.mubble.item.weapon.SplatoonWeaponItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class MubbleItems {
    // SUPER MARIO
    public static final Item MAKER_GLOVE = of(MubbleItemKeys.MAKER_GLOVE, new Item.Settings().maxCount(1));
    public static final CapeFeatherItem CAPE_FEATHER = of(MubbleItemKeys.CAPE_FEATHER, s -> new CapeFeatherItem(s, false));
    public static final CapeFeatherItem SUPER_CAPE_FEATHER = of(MubbleItemKeys.SUPER_CAPE_FEATHER, s -> new CapeFeatherItem(s.rarity(Rarity.EPIC), true));

    public static final SpawnEggItem GOOMBA_SPAWN_EGG = of(MubbleItemKeys.GOOMBA_SPAWN_EGG, s -> new SpawnEggItem(MubbleEntityTypes.GOOMBA, 0x96664f, 0xf3dca6, s));

    // SPLATOON
    public static final SplatoonWeaponItem SPLATTERSHOT = of(MubbleItemKeys.SPLATTERSHOT, AutomaticShooterConfig.ofSplat(ShooterInkBulletConfig.ofSplat(40, 8, 360, 180, 1.4495F, 4, 2.2F, 0.016f), 6, 12.0F, 6.0F));
    public static final SplatoonWeaponItem DOT_96_GAL = of(MubbleItemKeys.DOT_96_GAL, AutomaticShooterConfig.ofSplat(ShooterInkBulletConfig.ofSplat(25, 9, 620, 350, 2.377F, 5, 2.45F, 0.016f), 12, 11.3511F, 4.0F));
    public static final SplatoonWeaponItem TEST_SHOOTER = of(MubbleItemKeys.TEST_SHOOTER, AutomaticShooterConfig.of(ShooterInkBulletConfig.of(10.0F, 5.0F, 2, 5, 1.0f, 10, 3.0f, 0.1f), 2, 0.0F, 0.0F));

    private static <O extends Item> O of(RegistryKey<Item> key, Function<Item.Settings, O> factory, Item.Settings settings) {
        return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
    }

    private static <O extends Item> O of(RegistryKey<Item> key, Function<Item.Settings, O> factory) {
        return of(key, factory, new Item.Settings());
    }

    private static Item of(RegistryKey<Item> key, Item.Settings settings) {
        return of(key, Item::new, settings.registryKey(key));
    }

    private static Item of(RegistryKey<Item> key) {
        return of(key, new Item.Settings());
    }

    private static SplatoonWeaponItem of(RegistryKey<Item> key, SplatoonWeapon weapon) {
        return of(key, SplatoonWeaponItem::new, new Item.Settings().maxCount(1).component(MubbleDataComponentsTypes.SPLATOON_WEAPON, RegistryEntry.of(weapon)));
    }
}
