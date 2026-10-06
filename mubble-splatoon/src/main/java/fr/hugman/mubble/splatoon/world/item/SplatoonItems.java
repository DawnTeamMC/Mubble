package fr.hugman.mubble.splatoon.world.item;

import fr.hugman.mubble.splatoon.core.component.SplatoonDataComponents;
import fr.hugman.mubble.splatoon.references.SplatoonItemKeys;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBulletConfig;
import fr.hugman.mubble.splatoon.world.item.weapon.AutomaticShooterConfig;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletCollisionParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletDamageParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletMoveParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.BulletPaintParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.SplashPaintParam;
import fr.hugman.mubble.splatoon.world.item.weapon.param.SplashSpawnParam;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.UseEffects;

import java.util.function.Function;

public class SplatoonItems {
    public static final SplatoonWeaponItem SPLATTERSHOT = register(SplatoonItemKeys.SPLATTERSHOT,
            new AutomaticShooterConfig(ShooterInkBulletConfig.SPLATTERSHOT, AutomaticShooterConfig.DEFAULT_REPEAT_FRAME, 4.86F, 11.66F, 0.072F, 2.0F));
    public static final SplatoonWeaponItem DOT_96_GAL = register(SplatoonItemKeys.DOT_96_GAL,
            new AutomaticShooterConfig(ShooterInkBulletConfig.DOT_96_GAL, 12, 4.0F, 11.3511F, 0.04F, 2.0F));
    /**
     * Fires fast, straight and true, for testing.
     */
    public static final SplatoonWeaponItem TEST_SHOOTER = register(SplatoonItemKeys.TEST_SHOOTER, new AutomaticShooterConfig(
            new ShooterInkBulletConfig(
                    BulletMoveParam.of(1.0F, 30, 1.0F, BulletMoveParam.DEFAULT_FREE_GRAVITY),
                    new BulletDamageParam(100, 50, 10, 30),
                    BulletCollisionParam.DEFAULT,
                    BulletPaintParam.of(1.0F, 1.0F, 1.0F, 1.1F, 1.0F, 1.5F, 1.0F, 1.5F),
                    SplashSpawnParam.NONE,
                    new SplashPaintParam(1.0F, 1.0F, 3.0F, 10.0F)
            ), 2, 0.0F, 0.0F, 0.072F, 0.0F));

    private static SplatoonWeaponItem register(ResourceKey<Item> key, AutomaticShooterConfig weapon) {
        return register(key, SplatoonWeaponItem::new, new Item.Properties().stacksTo(1)
                .component(SplatoonDataComponents.SPLATOON_WEAPON, Holder.direct(weapon))
                .component(SplatoonDataComponents.INK, InkStyle.DEFAULT)
                // walking while firing, never running
                .component(DataComponents.USE_EFFECTS, new UseEffects(false, true, weapon.walkSpeedMultiplier())));
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
