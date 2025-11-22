package fr.hugman.mubble.item;

import fr.hugman.mubble.component.MubbleDataComponentTypes;
import fr.hugman.mubble.component.PowerUpComponent;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.power_up.PowerUps;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import fr.hugman.mubble.item.helper.ItemFactory;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.util.Rarity;

import java.util.Optional;
import java.util.function.Function;

public class MubbleItems {
    // SUPER MARIO
    public static final Item MAKER_GLOVE = of(MubbleItemKeys.MAKER_GLOVE, new Item.Settings().maxCount(1));
    public static final KoopaShellItem GREEN_KOOPA_SHELL = of(MubbleItemKeys.GREEN_KOOPA_SHELL, s -> new KoopaShellItem(s, false), new Item.Settings().maxCount(3));
    public static final KoopaShellItem RED_KOOPA_SHELL = of(MubbleItemKeys.RED_KOOPA_SHELL, s -> new KoopaShellItem(s, true), new Item.Settings().maxCount(3));

    public static final PowerUpItem MINI_MUSHROOM = powerUp(MubbleItemKeys.MINI_MUSHROOM, PowerUps.MINI);
    public static final PowerUpItem MEGA_MUSHROOM = powerUp(MubbleItemKeys.MEGA_MUSHROOM, PowerUps.MEGA);
    public static final PowerUpItem FIRE_FLOWER = powerUp(MubbleItemKeys.FIRE_FLOWER, PowerUps.FIRE);
    public static final PowerUpItem ICE_FLOWER = powerUp(MubbleItemKeys.ICE_FLOWER, PowerUps.ICE);
    public static final CapeFeatherItem CAPE_FEATHER = of(MubbleItemKeys.CAPE_FEATHER, s -> new CapeFeatherItem(s, false));
    public static final CapeFeatherItem SUPER_CAPE_FEATHER = of(MubbleItemKeys.SUPER_CAPE_FEATHER, s -> new CapeFeatherItem(s.rarity(Rarity.EPIC), true));

    public static final SpawnEggItem GOOMBA_SPAWN_EGG = of(MubbleItemKeys.GOOMBA_SPAWN_EGG, ItemFactory.spawnEgg(MubbleEntityTypes.GOOMBA));

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

    private static PowerUpItem powerUp(RegistryKey<Item> key, RegistryKey<PowerUp> powerUp) {
        return of(key, PowerUpItem::new, new Item.Settings()
                .component(MubbleDataComponentTypes.POWER_UP, new PowerUpComponent(new LazyRegistryEntryReference<>(powerUp)))
                .component(DataComponentTypes.USE_COOLDOWN, new UseCooldownComponent(1.0f, Optional.of(MubbleCooldownGroups.POWER_UPS))));
    }
}
