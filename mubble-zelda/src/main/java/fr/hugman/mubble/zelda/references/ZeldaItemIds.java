package fr.hugman.mubble.zelda.references;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ZeldaItemIds {
    public static final ResourceKey<Item> LOFTWING_SPAWN_EGG = createKey("loftwing_spawn_egg");

    private static ResourceKey<Item> createKey(String path) {
        return ResourceKey.create(Registries.ITEM, Zelda.id(path));
    }
}
