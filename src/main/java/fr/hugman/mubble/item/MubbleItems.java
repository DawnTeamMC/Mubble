package fr.hugman.mubble.item;

import fr.hugman.mubble.entity.MubbleEntityTypes;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class MubbleItems {
    // SUPER MARIO
    public static final Item MAKER_GLOVE = of(MubbleItemKeys.MAKER_GLOVE, new Item.Settings().maxCount(1));
    public static final KoopaShellItem GREEN_KOOPA_SHELL = of(MubbleItemKeys.GREEN_KOOPA_SHELL, s -> new KoopaShellItem(s, false), new Item.Settings().maxCount(3));
    public static final KoopaShellItem RED_KOOPA_SHELL = of(MubbleItemKeys.RED_KOOPA_SHELL, s -> new KoopaShellItem(s, true), new Item.Settings().maxCount(3));
    public static final CapeFeatherItem CAPE_FEATHER = of(MubbleItemKeys.CAPE_FEATHER, s -> new CapeFeatherItem(s, false));
    public static final CapeFeatherItem SUPER_CAPE_FEATHER = of(MubbleItemKeys.SUPER_CAPE_FEATHER, s -> new CapeFeatherItem(s.rarity(Rarity.EPIC), true));

    public static final SpawnEggItem GOOMBA_SPAWN_EGG = of(MubbleItemKeys.GOOMBA_SPAWN_EGG, s -> new SpawnEggItem(MubbleEntityTypes.GOOMBA, 0x96664f, 0xf3dca6, s));

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
}
