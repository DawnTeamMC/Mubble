package fr.hugman.mubble.item_group;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class MubbleItemGroupKeys {
    public static final ResourceKey<CreativeModeTab> SUPER_MARIO = of("super_mario");
    public static final ResourceKey<CreativeModeTab> YOSHI_ISLAND = of("yoshi_island");

    private static ResourceKey<CreativeModeTab> of(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Mubble.id(path));
    }
}
