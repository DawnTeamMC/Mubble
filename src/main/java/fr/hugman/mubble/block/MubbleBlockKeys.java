package fr.hugman.mubble.block;

import fr.hugman.mubble.Mubble;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MubbleBlockKeys {
    // SUPER MARIO
    public static final RegistryKey<Block> EMPTY_BLOCK = of("empty_block");
    public static final RegistryKey<Block> QUESTION_BLOCK = of("question_block");
    public static final RegistryKey<Block> BRICK_BLOCK = of("brick_block");
    public static final RegistryKey<Block> CRYSTAL_BLOCK = of("crystal_block");
    public static final RegistryKey<Block> GOLD_BLOCK = of("gold_block");
    public static final RegistryKey<Block> BLUE_EXCLAMATION_BLOCK = of("blue_exclamation_block");
    public static final RegistryKey<Block> GREEN_EXCLAMATION_BLOCK = of("green_exclamation_block");
    public static final RegistryKey<Block> YELLOW_EXCLAMATION_BLOCK = of("yellow_exclamation_block");
    public static final RegistryKey<Block> RED_EXCLAMATION_BLOCK = of("red_exclamation_block");
    public static final RegistryKey<Block> NOTE_BLOCK = of("note_block");
    public static final RegistryKey<Block> BLUE_MARIMBA_BLOCK = of("blue_marimba_block");
    public static final RegistryKey<Block> GREEN_MARIMBA_BLOCK = of("green_marimba_block");
    public static final RegistryKey<Block> YELLOW_MARIMBA_BLOCK = of("yellow_marimba_block");
    public static final RegistryKey<Block> RED_MARIMBA_BLOCK = of("red_marimba_block");
    public static final RegistryKey<Block> SNAKE_BLOCK = of("snake_block");
    public static final RegistryKey<Block> FAST_SNAKE_BLOCK = of("fast_snake_block");
    public static final RegistryKey<Block> SLOW_SNAKE_BLOCK = of("slow_snake_block");
    public static final RegistryKey<Block> RED_BEEP_BLOCK = of("red_beep_block");
    public static final RegistryKey<Block> BLUE_BEEP_BLOCK = of("blue_beep_block");

    // YOSHI'S ISLAND
    public static final RegistryKey<Block> BLUE_EGG_BLOCK = of("blue_egg_block");
    public static final RegistryKey<Block> CYAN_EGG_BLOCK = of("cyan_egg_block");
    public static final RegistryKey<Block> GREEN_EGG_BLOCK = of("green_egg_block");
    public static final RegistryKey<Block> YELLOW_EGG_BLOCK = of("yellow_egg_block");
    public static final RegistryKey<Block> ORANGE_EGG_BLOCK = of("orange_egg_block");
    public static final RegistryKey<Block> RED_EGG_BLOCK = of("red_egg_block");
    public static final RegistryKey<Block> PINK_EGG_BLOCK = of("pink_egg_block");
    public static final RegistryKey<Block> BLACK_EGG_BLOCK = of("black_egg_block");
    public static final RegistryKey<Block> WHITE_EGG_BLOCK = of("white_egg_block");

    // SPLATOON
    public static final RegistryKey<Block> INK = of("ink");

    private static RegistryKey<Block> of(String path) {
        return RegistryKey.of(RegistryKeys.BLOCK, Mubble.id(path));
    }
}
