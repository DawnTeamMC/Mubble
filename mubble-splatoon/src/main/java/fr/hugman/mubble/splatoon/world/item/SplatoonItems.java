package fr.hugman.mubble.splatoon.world.item;

import fr.hugman.mubble.splatoon.core.component.SplatoonDataComponents;
import fr.hugman.mubble.splatoon.references.SplatoonItemKeys;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBulletConfig;
import fr.hugman.mubble.splatoon.world.item.weapon.AutomaticShooterConfig;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeapon;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class SplatoonItems {
    public static final SplatoonWeaponItem SPLATTERSHOT = register(SplatoonItemKeys.SPLATTERSHOT, AutomaticShooterConfig.ofSplat(ShooterInkBulletConfig.ofSplat(40, 8, 360, 180, 1.4495F, 4, 2.2F, 0.016f), 6, 12.0F, 6.0F));
    public static final SplatoonWeaponItem DOT_96_GAL = register(SplatoonItemKeys.DOT_96_GAL, AutomaticShooterConfig.ofSplat(ShooterInkBulletConfig.ofSplat(25, 9, 620, 350, 2.377F, 5, 2.45F, 0.016f), 12, 11.3511F, 4.0F));
    public static final SplatoonWeaponItem TEST_SHOOTER = register(SplatoonItemKeys.TEST_SHOOTER, AutomaticShooterConfig.of(ShooterInkBulletConfig.of(10.0F, 5.0F, 2, 5, 1.0f, 10, 3.0f, 0.1f), 2, 0.0F, 0.0F));

    private static SplatoonWeaponItem register(ResourceKey<Item> key, SplatoonWeapon weapon) {
        return register(key, SplatoonWeaponItem::new, new Item.Properties().stacksTo(1).component(SplatoonDataComponents.SPLATOON_WEAPON, Holder.direct(weapon)));
    }

    private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory, Item.Properties settings) {
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(settings.setId(key)));
    }

    private static <O extends Item> O register(ResourceKey<Item> key, Function<Item.Properties, O> factory) {
        return register(key, factory, new Item.Properties());
    }

    private static Item register(ResourceKey<Item> key, Item.Properties settings) {
        return register(key, Item::new, settings.setId(key));
    }

    private static Item register(ResourceKey<Item> key) {
        return register(key, new Item.Properties());
    }
}
