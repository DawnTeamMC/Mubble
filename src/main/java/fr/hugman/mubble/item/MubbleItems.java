package fr.hugman.mubble.item;

import fr.hugman.mubble.entity.projectile.ShooterInkBulletConfig;
import fr.hugman.mubble.item.weapon.AutomaticShooterConfig;
import fr.hugman.mubble.item.weapon.AutomaticShooterItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Rarity;

public class MubbleItems {
    // SUPER MARIO
    public static final Item MAKER_GLOVE = of(MubbleItemKeys.MAKER_GLOVE, new Item.Settings().maxCount(1));
    public static final CapeFeatherItem CAPE_FEATHER = of(MubbleItemKeys.CAPE_FEATHER, new CapeFeatherItem(new Item.Settings(), false));
    public static final CapeFeatherItem SUPER_CAPE_FEATHER = of(MubbleItemKeys.SUPER_CAPE_FEATHER, new CapeFeatherItem(new Item.Settings().rarity(Rarity.EPIC), true));

    // SPLATOON
    public static final AutomaticShooterItem SPLATTERSHOT = of(MubbleItemKeys.SPLATTERSHOT, new AutomaticShooterItem(AutomaticShooterConfig.ofSplat(ShooterInkBulletConfig.ofSplat(40, 8, 360, 180, 1.4495F, 4, 2.2F, 0.016f), 6, 12.0F, 6.0F)));
    public static final AutomaticShooterItem DOT_96_GAL = of(MubbleItemKeys.DOT_96_GAL, new AutomaticShooterItem(AutomaticShooterConfig.ofSplat(ShooterInkBulletConfig.ofSplat(25, 9, 620, 350, 2.377F, 5, 2.45F, 0.016f), 12, 11.3511F, 4.0F)));
    public static final AutomaticShooterItem TEST_SHOOTER = of(MubbleItemKeys.TEST_SHOOTER, new AutomaticShooterItem(AutomaticShooterConfig.of(ShooterInkBulletConfig.of(10.0F, 5.0F, 2, 5, 1.0f, 10, 3.0f, 0.1f), 2, 0.0F, 0.0F)));

    private static <O extends Item> O of(RegistryKey<Item> key, O item) {
        return Registry.register(Registries.ITEM, key, item);
    }

    private static Item of(RegistryKey<Item> key, Item.Settings settings) {
        return of(key, new Item(settings));
    }

    private static Item of(RegistryKey<Item> key) {
        return of(key, new Item.Settings());
    }
}
