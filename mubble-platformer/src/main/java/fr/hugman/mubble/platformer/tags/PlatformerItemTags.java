package fr.hugman.mubble.platformer.tags;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class PlatformerItemTags {
    /** Items the orbit camera aims over the shoulder for as long as they are held, on top of bows, crossbows and tridents. */
    public static final TagKey<Item> AIMS_WHILE_HELD = bind("aims_while_held");

    public static TagKey<Item> bind(String path) {
        return TagKey.create(Registries.ITEM, Mubble.id(path));
    }
}
