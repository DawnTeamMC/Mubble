package fr.hugman.mubble.fortnite.references;

import fr.hugman.mubble.fortnite.Fortnite;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class FortniteItemIds {
    public static final ResourceKey<Item> IMPULSE_GRENADE = createKey("impulse_grenade");

    private static ResourceKey<Item> createKey(String path) {
        return ResourceKey.create(Registries.ITEM, Fortnite.id(path));
    }
}
