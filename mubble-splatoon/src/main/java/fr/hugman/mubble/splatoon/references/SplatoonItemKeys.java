package fr.hugman.mubble.splatoon.references;

import fr.hugman.mubble.splatoon.Splatoon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class SplatoonItemKeys {
    public static final ResourceKey<Item> SPLATTERSHOT = createKey("splattershot");
    public static final ResourceKey<Item> DOT_96_GAL = createKey("dot_96_gal");
    public static final ResourceKey<Item> TEST_SHOOTER = createKey("test_shooter");

    private static ResourceKey<Item> createKey(String path) {
        return ResourceKey.create(Registries.ITEM, Splatoon.id(path));
    }
}
