package fr.hugman.mubble.fortnite.references;

import fr.hugman.mubble.fortnite.Fortnite;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class FortniteCreativeModeTabIds {
    public static final ResourceKey<CreativeModeTab> FORTNITE = createKey("fortnite");

    private static ResourceKey<CreativeModeTab> createKey(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Fortnite.id(path));
    }
}
