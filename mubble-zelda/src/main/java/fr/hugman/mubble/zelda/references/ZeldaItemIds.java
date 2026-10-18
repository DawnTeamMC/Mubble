package fr.hugman.mubble.zelda.references;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ZeldaItemIds {
    public static final ResourceKey<Item> MUSHROOM_SPORES_BOTTLE = createKey("mushroom_spores_bottle");

    public static final ResourceKey<Item> LOFTWING_WHISTLE = createKey("loftwing_whistle");
    public static final ResourceKey<Item> LOFTWING_BELT = createKey("loftwing_belt");

    public static final ResourceKey<Item> LEATHER_LOFTWING_HELMET = createKey("leather_loftwing_helmet");
    public static final ResourceKey<Item> GOLDEN_LOFTWING_HELMET = createKey("golden_loftwing_helmet");
    public static final ResourceKey<Item> CHAINMAIL_LOFTWING_HELMET = createKey("chainmail_loftwing_helmet");
    public static final ResourceKey<Item> IRON_LOFTWING_HELMET = createKey("iron_loftwing_helmet");
    public static final ResourceKey<Item> DIAMOND_LOFTWING_HELMET = createKey("diamond_loftwing_helmet");
    public static final ResourceKey<Item> NETHERITE_LOFTWING_HELMET = createKey("netherite_loftwing_helmet");

    public static final ResourceKey<Item> LOFTWING_SPAWN_EGG = createKey("loftwing_spawn_egg");

    private static ResourceKey<Item> createKey(String path) {
        return ResourceKey.create(Registries.ITEM, Zelda.id(path));
    }
}
