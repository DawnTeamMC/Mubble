package fr.hugman.mubble.testmod.references;

import fr.hugman.mubble.testmod.MubbleTestMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class TestModCreativeModeTabIds {
    public static final ResourceKey<CreativeModeTab> SANDBOX = createKey("sandbox");

    private static ResourceKey<CreativeModeTab> createKey(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, MubbleTestMod.id(path));
    }
}
