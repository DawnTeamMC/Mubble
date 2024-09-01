package fr.hugman.mubble.block;

import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;

public class MubbleBlocks {
    // SUPER MARIO
    public static final EmptyBlock EMPTY_BLOCK = of(MubbleBlockKeys.EMPTY_BLOCK, new EmptyBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN)));
    public static final DecoratedBumpableBlock QUESTION_BLOCK = of(MubbleBlockKeys.QUESTION_BLOCK, new DecoratedBumpableBlock(EMPTY_BLOCK.getDefaultState(), AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final DecoratedBumpableBlock BRICK_BLOCK = of(MubbleBlockKeys.BRICK_BLOCK, new DecoratedBumpableBlock(Blocks.AIR.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN)),new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final DecoratedBumpableBlock CRYSTAL_BLOCK = of(MubbleBlockKeys.CRYSTAL_BLOCK, new DecoratedBumpableBlock(Blocks.AIR.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BRICKS).sounds(BlockSoundGroup.AMETHYST_BLOCK).mapColor(MapColor.PURPLE)),new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final DecoratedBumpableBlock GOLD_BLOCK = of(MubbleBlockKeys.GOLD_BLOCK, new DecoratedBumpableBlock(Blocks.AIR.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.GOLD)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final DecoratedBumpableBlock BLUE_EXCLAMATION_BLOCK = exclamationBlock(MubbleBlockKeys.BLUE_EXCLAMATION_BLOCK, MapColor.BLUE);
    public static final DecoratedBumpableBlock GREEN_EXCLAMATION_BLOCK = exclamationBlock(MubbleBlockKeys.GREEN_EXCLAMATION_BLOCK, MapColor.GREEN);
    public static final DecoratedBumpableBlock YELLOW_EXCLAMATION_BLOCK = exclamationBlock(MubbleBlockKeys.YELLOW_EXCLAMATION_BLOCK, MapColor.YELLOW);
    public static final DecoratedBumpableBlock RED_EXCLAMATION_BLOCK = exclamationBlock(MubbleBlockKeys.RED_EXCLAMATION_BLOCK, MapColor.RED);
    public static final NoteBlock NOTE_BLOCK = noteBlock(MubbleBlockKeys.NOTE_BLOCK, MapColor.WHITE);
    public static final NoteBlock BLUE_MARIMBA_BLOCK = marimbaBlock(MubbleBlockKeys.BLUE_MARIMBA_BLOCK, MapColor.BLUE);
    public static final NoteBlock GREEN_MARIMBA_BLOCK = marimbaBlock(MubbleBlockKeys.GREEN_MARIMBA_BLOCK, MapColor.GREEN);
    public static final NoteBlock YELLOW_MARIMBA_BLOCK = marimbaBlock(MubbleBlockKeys.YELLOW_MARIMBA_BLOCK, MapColor.YELLOW);
    public static final NoteBlock RED_MARIMBA_BLOCK = marimbaBlock(MubbleBlockKeys.RED_MARIMBA_BLOCK, MapColor.RED);
    public static final SnakeBlock SNAKE_BLOCK = of(MubbleBlockKeys.SNAKE_BLOCK, new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME)));
    public static final SnakeBlock FAST_SNAKE_BLOCK = of(MubbleBlockKeys.FAST_SNAKE_BLOCK, new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LAPIS_BLUE)));
    public static final SnakeBlock SLOW_SNAKE_BLOCK = of(MubbleBlockKeys.SLOW_SNAKE_BLOCK, new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.RED)));
    public static final BeepBlock RED_BEEP_BLOCK = of(MubbleBlockKeys.RED_BEEP_BLOCK, new BeepBlock(BeepBlock.settings(MapColor.RED), false));
    public static final BeepBlock BLUE_BEEP_BLOCK = of(MubbleBlockKeys.BLUE_BEEP_BLOCK, new BeepBlock(BeepBlock.settings(MapColor.BLUE), true));

    // YOSHI'S ISLAND
    public static final DecoratedBumpableBlock BLUE_EGG_BLOCK = eggBlock(MubbleBlockKeys.BLUE_EGG_BLOCK, MapColor.BLUE);
    public static final DecoratedBumpableBlock CYAN_EGG_BLOCK = eggBlock(MubbleBlockKeys.CYAN_EGG_BLOCK, MapColor.CYAN);
    public static final DecoratedBumpableBlock GREEN_EGG_BLOCK = eggBlock(MubbleBlockKeys.GREEN_EGG_BLOCK, MapColor.GREEN);
    public static final DecoratedBumpableBlock YELLOW_EGG_BLOCK = eggBlock(MubbleBlockKeys.YELLOW_EGG_BLOCK, MapColor.YELLOW);
    public static final DecoratedBumpableBlock ORANGE_EGG_BLOCK = eggBlock(MubbleBlockKeys.ORANGE_EGG_BLOCK, MapColor.ORANGE);
    public static final DecoratedBumpableBlock RED_EGG_BLOCK = eggBlock(MubbleBlockKeys.RED_EGG_BLOCK, MapColor.RED);
    public static final DecoratedBumpableBlock PINK_EGG_BLOCK = eggBlock(MubbleBlockKeys.PINK_EGG_BLOCK, MapColor.PINK);
    public static final DecoratedBumpableBlock BLACK_EGG_BLOCK = eggBlock(MubbleBlockKeys.BLACK_EGG_BLOCK, MapColor.BLACK);
    public static final DecoratedBumpableBlock WHITE_EGG_BLOCK = eggBlock(MubbleBlockKeys.WHITE_EGG_BLOCK, MapColor.WHITE);

    private static <O extends Block> O noItem(RegistryKey<Block> key, O block) {
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static <O extends Block> O of(RegistryKey<Block> key, O block, Item.Settings settings) {
        noItem(key, block);
        Registry.register(Registries.ITEM, key.getValue(), new BlockItem(block, settings));
        return block;
    }

    private static <O extends Block> O of(RegistryKey<Block> key, O block) {
        return of(key, block, new Item.Settings());
    }

    private static DecoratedBumpableBlock exclamationBlock(RegistryKey<Block> key, MapColor mapColor) {
        return of(key, new DecoratedBumpableBlock(null, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(mapColor)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    }

    private static NoteBlock noteBlock(RegistryKey<Block> key, MapColor mapColor) {
        return of(key, new NoteBlock(MubbleSounds.NOTE_BLOCK_JUMP_LOW, MubbleSounds.NOTE_BLOCK_JUMP_HIGH, AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK).mapColor(mapColor)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    }

    private static NoteBlock marimbaBlock(RegistryKey<Block> key, MapColor mapColor) {
        //TODO: temporary behavior. Change this to the actual Mario Wonder behaviour
        return noteBlock(key, mapColor);
    }

    private static DecoratedBumpableBlock eggBlock(RegistryKey<Block> key, MapColor mapColor) {
        return of(key, new DecoratedBumpableBlock(null, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(mapColor)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    }
}
