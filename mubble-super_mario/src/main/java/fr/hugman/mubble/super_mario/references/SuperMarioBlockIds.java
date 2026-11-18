package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class SuperMarioBlockIds {
    /** The route a snake walks, drawn one block at a time. Never held as an item. */
    public static final ResourceKey<Block> SNAKE_PATH = createKey("snake_path");
    /** Stands in for the head and the tail of a running snake while they are between two cells. */
    public static final ResourceKey<Block> SNAKE_MOVING_BLOCK = createKey("snake_moving_block");

    private static ResourceKey<Block> createKey(String path) {
        return ResourceKey.create(Registries.BLOCK, SuperMario.id(path));
    }
}
