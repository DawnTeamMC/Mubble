package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class MubbleBlockKeys {
    // SUPER MARIO
    public static final ResourceKey<Block> EMPTY_BLOCK = createKey("empty_block");
    public static final ResourceKey<Block> QUESTION_BLOCK = createKey("question_block");
    public static final ResourceKey<Block> BRICK_BLOCK = createKey("brick_block");
    public static final ResourceKey<Block> CRYSTAL_BLOCK = createKey("crystal_block");
    public static final ResourceKey<Block> GOLD_BLOCK = createKey("gold_block");
    public static final ResourceKey<Block> BLUE_EXCLAMATION_BLOCK = createKey("blue_exclamation_block");
    public static final ResourceKey<Block> GREEN_EXCLAMATION_BLOCK = createKey("green_exclamation_block");
    public static final ResourceKey<Block> YELLOW_EXCLAMATION_BLOCK = createKey("yellow_exclamation_block");
    public static final ResourceKey<Block> RED_EXCLAMATION_BLOCK = createKey("red_exclamation_block");
    public static final ResourceKey<Block> NOTE_BLOCK = createKey("note_block");
    public static final ResourceKey<Block> BLUE_MARIMBA_BLOCK = createKey("blue_marimba_block");
    public static final ResourceKey<Block> GREEN_MARIMBA_BLOCK = createKey("green_marimba_block");
    public static final ResourceKey<Block> YELLOW_MARIMBA_BLOCK = createKey("yellow_marimba_block");
    public static final ResourceKey<Block> RED_MARIMBA_BLOCK = createKey("red_marimba_block");
    public static final ResourceKey<Block> SNAKE_BLOCK = createKey("snake_block");
    public static final ResourceKey<Block> FAST_SNAKE_BLOCK = createKey("fast_snake_block");
    public static final ResourceKey<Block> SLOW_SNAKE_BLOCK = createKey("slow_snake_block");
    public static final ResourceKey<Block> RED_BEEP_BLOCK = createKey("red_beep_block");
    public static final ResourceKey<Block> BLUE_BEEP_BLOCK = createKey("blue_beep_block");

    // YOSHI'S ISLAND
    public static final ResourceKey<Block> BLUE_EGG_BLOCK = createKey("blue_egg_block");
    public static final ResourceKey<Block> CYAN_EGG_BLOCK = createKey("cyan_egg_block");
    public static final ResourceKey<Block> GREEN_EGG_BLOCK = createKey("green_egg_block");
    public static final ResourceKey<Block> YELLOW_EGG_BLOCK = createKey("yellow_egg_block");
    public static final ResourceKey<Block> ORANGE_EGG_BLOCK = createKey("orange_egg_block");
    public static final ResourceKey<Block> RED_EGG_BLOCK = createKey("red_egg_block");
    public static final ResourceKey<Block> PINK_EGG_BLOCK = createKey("pink_egg_block");
    public static final ResourceKey<Block> BLACK_EGG_BLOCK = createKey("black_egg_block");
    public static final ResourceKey<Block> WHITE_EGG_BLOCK = createKey("white_egg_block");

    private static ResourceKey<Block> createKey(String path) {
        return ResourceKey.create(Registries.BLOCK, Mubble.id(path));
    }
}
