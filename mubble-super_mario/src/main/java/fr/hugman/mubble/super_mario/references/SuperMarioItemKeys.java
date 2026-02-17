package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class SuperMarioItemKeys {
    public static final ResourceKey<Item> COIN = createKey("coin");
    public static final ResourceKey<Item> RED_COIN = createKey("red_coin");
    public static final ResourceKey<Item> BLUE_COIN = createKey("blue_coin");
    public static final ResourceKey<Item> FLOWER_COIN = createKey("flower_coin");
    public static final ResourceKey<Item> GREEN_KOOPA_SHELL = createKey("green_koopa_shell");
	public static final ResourceKey<Item> RED_KOOPA_SHELL = createKey("red_koopa_shell");

    public static final ResourceKey<Item> SUPER_MUSHROOM = createKey("super_mushroom");
    public static final ResourceKey<Item> MINI_MUSHROOM = createKey("mini_mushroom");
    public static final ResourceKey<Item> MEGA_MUSHROOM = createKey("mega_mushroom");
    public static final ResourceKey<Item> FIRE_FLOWER = createKey("fire_flower");
    public static final ResourceKey<Item> ICE_FLOWER = createKey("ice_flower");
    public static final ResourceKey<Item> GOLD_FLOWER = createKey("gold_flower");
    public static final ResourceKey<Item> CLOUD_FLOWER = createKey("cloud_flower");

    public static final ResourceKey<Item> CAPE_FEATHER = createKey("cape_feather");
    public static final ResourceKey<Item> SUPER_CAPE_FEATHER = createKey("super_cape_feather");
    public static final ResourceKey<Item> GOOMBA_SPAWN_EGG = createKey("goomba_spawn_egg");

	public static final ResourceKey<Item> MAKER_GLOVE = createKey("maker_glove");

    private static ResourceKey<Item> createKey(String path) {
        return ResourceKey.create(Registries.ITEM, SuperMario.id(path));
    }
}
