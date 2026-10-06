package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class SuperMarioBlockItemIds {
    public static final BlockItemId EMPTY_BLOCK = createKey("empty_block");
    public static final BlockItemId QUESTION_BLOCK = createKey("question_block");
    public static final BlockItemId BRICK_BLOCK = createKey("brick_block");
    public static final BlockItemId CRYSTAL_BLOCK = createKey("crystal_block");
    public static final BlockItemId GOLD_BLOCK = createKey("gold_block");
    public static final BlockItemId BLUE_EXCLAMATION_BLOCK = createKey("blue_exclamation_block");
    public static final BlockItemId GREEN_EXCLAMATION_BLOCK = createKey("green_exclamation_block");
    public static final BlockItemId YELLOW_EXCLAMATION_BLOCK = createKey("yellow_exclamation_block");
    public static final BlockItemId RED_EXCLAMATION_BLOCK = createKey("red_exclamation_block");
    public static final BlockItemId NOTE_BLOCK = createKey("note_block");
    public static final BlockItemId BLUE_MARIMBA_BLOCK = createKey("blue_marimba_block");
    public static final BlockItemId GREEN_MARIMBA_BLOCK = createKey("green_marimba_block");
    public static final BlockItemId YELLOW_MARIMBA_BLOCK = createKey("yellow_marimba_block");
    public static final BlockItemId RED_MARIMBA_BLOCK = createKey("red_marimba_block");
    public static final BlockItemId SNAKE_BLOCK = createKey("snake_block");
    public static final BlockItemId FAST_SNAKE_BLOCK = createKey("fast_snake_block");
    public static final BlockItemId SLOW_SNAKE_BLOCK = createKey("slow_snake_block");
    public static final BlockItemId RED_BEEP_BLOCK = createKey("red_beep_block");
    public static final BlockItemId BLUE_BEEP_BLOCK = createKey("blue_beep_block");

    public static final BlockItemId BLUE_EGG_BLOCK = createKey("blue_egg_block");
    public static final BlockItemId CYAN_EGG_BLOCK = createKey("cyan_egg_block");
    public static final BlockItemId GREEN_EGG_BLOCK = createKey("green_egg_block");
    public static final BlockItemId YELLOW_EGG_BLOCK = createKey("yellow_egg_block");
    public static final BlockItemId ORANGE_EGG_BLOCK = createKey("orange_egg_block");
    public static final BlockItemId RED_EGG_BLOCK = createKey("red_egg_block");
    public static final BlockItemId PINK_EGG_BLOCK = createKey("pink_egg_block");
    public static final BlockItemId BLACK_EGG_BLOCK = createKey("black_egg_block");
    public static final BlockItemId WHITE_EGG_BLOCK = createKey("white_egg_block");

    private static BlockItemId createKey(String path) {
        var id = SuperMario.id(path);
        return BlockItemId.create(id, id);
    }
}
