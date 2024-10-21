package fr.hugman.mubble.item;

import fr.hugman.mubble.Mubble;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MubbleItemKeys {
    // SUPER MARIO
    public static final RegistryKey<Item> MAKER_GLOVE = of("maker_glove");

    public static final RegistryKey<Item> MINI_MUSHROOM = of("mini_mushroom");
    public static final RegistryKey<Item> MEGA_MUSHROOM = of("mega_mushroom");

    public static final RegistryKey<Item> CAPE_FEATHER = of("cape_feather");
    public static final RegistryKey<Item> SUPER_CAPE_FEATHER = of("super_cape_feather");
    public static final RegistryKey<Item> GOOMBA_SPAWN_EGG = of("goomba_spawn_egg");

    private static RegistryKey<Item> of(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Mubble.id(path));
    }
}
