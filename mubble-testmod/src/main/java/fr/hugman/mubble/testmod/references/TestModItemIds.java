package fr.hugman.mubble.testmod.references;

import fr.hugman.mubble.testmod.MubbleTestMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class TestModItemIds {
    public static final ResourceKey<Item> SNOWBALL_FLOWER = createKey("snowball_flower");

    private static ResourceKey<Item> createKey(String path) {
        return ResourceKey.create(Registries.ITEM, MubbleTestMod.id(path));
    }
}
