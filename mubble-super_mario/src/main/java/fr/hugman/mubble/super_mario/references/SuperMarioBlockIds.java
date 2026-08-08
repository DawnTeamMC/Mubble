package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class SuperMarioBlockIds {
    private static ResourceKey<Block> createKey(String path) {
        return ResourceKey.create(Registries.BLOCK, SuperMario.id(path));
    }
}
