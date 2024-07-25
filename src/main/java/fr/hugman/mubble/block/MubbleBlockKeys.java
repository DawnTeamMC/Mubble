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
    public static final RegistryKey<Block> GOLD_BLOCK = of("gold_block");
    public static final RegistryKey<Block> NOTE_BLOCK = of("note_block");
    public static final RegistryKey<Block> EXCLAMATION_BLOCK = of("exclamation_block");
    public static final RegistryKey<Block> SNAKE_BLOCK = of("snake_block");
    public static final RegistryKey<Block> FAST_SNAKE_BLOCK = of("fast_snake_block");
    public static final RegistryKey<Block> SLOW_SNAKE_BLOCK = of("slow_snake_block");
    public static final RegistryKey<Block> RED_BEEP_BLOCK = of("red_beep_block");
    public static final RegistryKey<Block> BLUE_BEEP_BLOCK = of("blue_beep_block");

    // SPLATOON
    public static final RegistryKey<Block> INK = of("ink");

    private static RegistryKey<Block> of(String path) {
        return RegistryKey.of(RegistryKeys.BLOCK, Mubble.id(path));
    }
}
