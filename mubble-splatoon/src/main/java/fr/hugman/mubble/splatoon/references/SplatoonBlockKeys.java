package fr.hugman.mubble.splatoon.references;

import fr.hugman.mubble.splatoon.Splatoon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class SplatoonBlockKeys {
    public static final ResourceKey<Block> INK = createKey("ink");

    private static ResourceKey<Block> createKey(String path) {
        return ResourceKey.create(Registries.BLOCK, Splatoon.id(path));
    }
}
