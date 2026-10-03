package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class MubbleItemIds {
    public static final ResourceKey<Item> MARIO_BOOTS = createKey("mario_boots");

    private static ResourceKey<Item> createKey(String path) {
        return ResourceKey.create(Registries.ITEM, Mubble.id(path));
    }
}
