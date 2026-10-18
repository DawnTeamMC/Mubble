package fr.hugman.mubble.zelda.references;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class ZeldaCreativeModeTabIds {
    public static final ResourceKey<CreativeModeTab> ZELDA = createKey("zelda");

    private static ResourceKey<CreativeModeTab> createKey(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Zelda.id(path));
    }
}
