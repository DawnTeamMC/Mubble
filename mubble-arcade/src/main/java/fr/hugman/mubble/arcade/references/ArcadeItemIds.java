package fr.hugman.mubble.arcade.references;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ArcadeItemIds {
    public static final ResourceKey<Item> MARIO_BOOTS = createKey("mario_boots");

    private static ResourceKey<Item> createKey(String path) {
        return ResourceKey.create(Registries.ITEM, Mubble.id(path));
    }
}
