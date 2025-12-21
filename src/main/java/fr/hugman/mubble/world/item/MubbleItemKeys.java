package fr.hugman.mubble.world.item;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class MubbleItemKeys {
    // SUPER MARIO
    public static final ResourceKey<Item> MAKER_GLOVE = of("maker_glove");
    public static final ResourceKey<Item> GREEN_KOOPA_SHELL = of("green_koopa_shell");
    public static final ResourceKey<Item> RED_KOOPA_SHELL = of("red_koopa_shell");

    public static final ResourceKey<Item> MINI_MUSHROOM = of("mini_mushroom");
    public static final ResourceKey<Item> MEGA_MUSHROOM = of("mega_mushroom");
    public static final ResourceKey<Item> FIRE_FLOWER = of("fire_flower");
    public static final ResourceKey<Item> ICE_FLOWER = of("ice_flower");

    public static final ResourceKey<Item> CAPE_FEATHER = of("cape_feather");
    public static final ResourceKey<Item> SUPER_CAPE_FEATHER = of("super_cape_feather");
    public static final ResourceKey<Item> GOOMBA_SPAWN_EGG = of("goomba_spawn_egg");

    private static ResourceKey<Item> of(String path) {
        return ResourceKey.create(Registries.ITEM, Mubble.id(path));
    }
}
