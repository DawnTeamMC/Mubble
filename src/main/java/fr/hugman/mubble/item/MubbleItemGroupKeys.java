package fr.hugman.mubble.item;

import fr.hugman.mubble.Mubble;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MubbleItemGroupKeys {
    public static final RegistryKey<ItemGroup> SUPER_MARIO = of("super_mario");

    private static RegistryKey<ItemGroup> of(String path) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Mubble.id(path));
    }
}
