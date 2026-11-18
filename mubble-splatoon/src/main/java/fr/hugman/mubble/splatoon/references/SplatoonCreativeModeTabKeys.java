package fr.hugman.mubble.splatoon.references;

import fr.hugman.mubble.splatoon.Splatoon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class SplatoonCreativeModeTabKeys {
    public static final ResourceKey<CreativeModeTab> SPLATOON = createKey("splatoon");

    private static ResourceKey<CreativeModeTab> createKey(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Splatoon.id(path));
    }
}
