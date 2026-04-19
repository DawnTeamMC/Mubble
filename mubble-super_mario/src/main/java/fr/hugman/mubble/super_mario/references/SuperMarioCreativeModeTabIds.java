package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class SuperMarioCreativeModeTabIds {
    public static final ResourceKey<CreativeModeTab> SUPER_MARIO = createKey("super_mario");
    public static final ResourceKey<CreativeModeTab> YOSHI_ISLAND = createKey("yoshi_island");

    private static ResourceKey<CreativeModeTab> createKey(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, SuperMario.id(path));
    }
}
