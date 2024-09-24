package fr.hugman.mubble.item;

import fr.hugman.mubble.entity.MubbleEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Rarity;

public class MubbleItems {
    // SUPER MARIO
    public static final Item MAKER_GLOVE = of(MubbleItemKeys.MAKER_GLOVE, new Item.Settings().maxCount(1));
    public static final KoopaShellItem GREEN_KOOPA_SHELL = of(MubbleItemKeys.GREEN_KOOPA_SHELL, new KoopaShellItem(new Item.Settings().maxCount(3)));
    public static final KoopaShellItem RED_KOOPA_SHELL = of(MubbleItemKeys.RED_KOOPA_SHELL, new KoopaShellItem(new Item.Settings().maxCount(3)));
    public static final CapeFeatherItem CAPE_FEATHER = of(MubbleItemKeys.CAPE_FEATHER, new CapeFeatherItem(new Item.Settings(), false));
    public static final CapeFeatherItem SUPER_CAPE_FEATHER = of(MubbleItemKeys.SUPER_CAPE_FEATHER, new CapeFeatherItem(new Item.Settings().rarity(Rarity.EPIC), true));

    public static final Item GOOMBA_SPAWN_EGG = of(MubbleItemKeys.GOOMBA_SPAWN_EGG, new SpawnEggItem(MubbleEntityTypes.GOOMBA, 0x96664f, 0xf3dca6, new Item.Settings()));

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
