package fr.hugman.mubble.block;

import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MubbleBlocks {
    // SUPER MARIO
    public static final EmptyBlock EMPTY_BLOCK = of(MubbleBlockKeys.EMPTY_BLOCK, EmptyBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN));
    public static final DecoratedBumpableBlock QUESTION_BLOCK = decoratedBumpableBlock(MubbleBlockKeys.QUESTION_BLOCK, EMPTY_BLOCK, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW));
    public static final DecoratedBumpableBlock BRICK_BLOCK = decoratedBumpableBlock(MubbleBlockKeys.BRICK_BLOCK, Blocks.AIR, AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN));
    public static final DecoratedBumpableBlock CRYSTAL_BLOCK = decoratedBumpableBlock(MubbleBlockKeys.CRYSTAL_BLOCK, Blocks.AIR, AbstractBlock.Settings.copy(Blocks.BRICKS).sounds(BlockSoundGroup.AMETHYST_BLOCK).mapColor(MapColor.PURPLE));
    public static final DecoratedBumpableBlock GOLD_BLOCK = decoratedBumpableBlock(MubbleBlockKeys.GOLD_BLOCK, Blocks.AIR, AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.GOLD));
    public static final DecoratedBumpableBlock BLUE_EXCLAMATION_BLOCK = exclamationBlock(MubbleBlockKeys.BLUE_EXCLAMATION_BLOCK, MapColor.BLUE);
    public static final DecoratedBumpableBlock GREEN_EXCLAMATION_BLOCK = exclamationBlock(MubbleBlockKeys.GREEN_EXCLAMATION_BLOCK, MapColor.GREEN);
    public static final DecoratedBumpableBlock YELLOW_EXCLAMATION_BLOCK = exclamationBlock(MubbleBlockKeys.YELLOW_EXCLAMATION_BLOCK, MapColor.YELLOW);
    public static final DecoratedBumpableBlock RED_EXCLAMATION_BLOCK = exclamationBlock(MubbleBlockKeys.RED_EXCLAMATION_BLOCK, MapColor.RED);
    public static final NoteBlock NOTE_BLOCK = noteBlock(MubbleBlockKeys.NOTE_BLOCK, MapColor.WHITE);
    public static final NoteBlock BLUE_MARIMBA_BLOCK = marimbaBlock(MubbleBlockKeys.BLUE_MARIMBA_BLOCK, MapColor.BLUE);
    public static final NoteBlock GREEN_MARIMBA_BLOCK = marimbaBlock(MubbleBlockKeys.GREEN_MARIMBA_BLOCK, MapColor.GREEN);
    public static final NoteBlock YELLOW_MARIMBA_BLOCK = marimbaBlock(MubbleBlockKeys.YELLOW_MARIMBA_BLOCK, MapColor.YELLOW);
    public static final NoteBlock RED_MARIMBA_BLOCK = marimbaBlock(MubbleBlockKeys.RED_MARIMBA_BLOCK, MapColor.RED);
    public static final SnakeBlock SNAKE_BLOCK = of(MubbleBlockKeys.SNAKE_BLOCK, SnakeBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME));
    public static final SnakeBlock FAST_SNAKE_BLOCK = of(MubbleBlockKeys.FAST_SNAKE_BLOCK, SnakeBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LAPIS_BLUE));
    public static final SnakeBlock SLOW_SNAKE_BLOCK = of(MubbleBlockKeys.SLOW_SNAKE_BLOCK, SnakeBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.RED));
    public static final BeepBlock RED_BEEP_BLOCK = of(MubbleBlockKeys.RED_BEEP_BLOCK, s -> new BeepBlock(s, false), BeepBlock.settings(MapColor.RED));
    public static final BeepBlock BLUE_BEEP_BLOCK = of(MubbleBlockKeys.BLUE_BEEP_BLOCK, s -> new BeepBlock(s, true), BeepBlock.settings(MapColor.BLUE));

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

    // SPLATOON
    public static final InkBlock INK_BLOCK = of(MubbleBlockKeys.INK, s -> new InkBlock(s), AbstractBlock.Settings.create().mapColor(MapColor.WATER_BLUE).replaceable().noCollision().strength(100.0f).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY));

    private static <B extends Block> B noItem(RegistryKey<Block> key, Function<AbstractBlock.Settings, B> factory, AbstractBlock.Settings blockSettings) {
        B block = factory.apply(blockSettings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static <B extends Block> B of(RegistryKey<Block> key, Function<AbstractBlock.Settings, B> factory, AbstractBlock.Settings blockSettings, Item.Settings itemSettings) {
        B block = noItem(key, factory, blockSettings);
        var itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
        Registry.register(Registries.ITEM, itemRegistryKey, new BlockItem(block, itemSettings.registryKey(itemRegistryKey).useBlockPrefixedTranslationKey()));
        return block;
    }

    private static <O extends Block> O of(RegistryKey<Block> key, Function<AbstractBlock.Settings, O> factory, AbstractBlock.Settings settings) {
        return of(key, factory, settings, new Item.Settings());
    }

    private static DecoratedBumpableBlock decoratedBumpableBlock(RegistryKey<Block> key, @Nullable BlockState defaultBumpedState, AbstractBlock.Settings settings) {
        return of(key, s -> new DecoratedBumpableBlock(defaultBumpedState, s), settings, new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    }

    private static DecoratedBumpableBlock decoratedBumpableBlock(RegistryKey<Block> key, Block defaultBumpedBlock, AbstractBlock.Settings settings) {
        return decoratedBumpableBlock(key, defaultBumpedBlock.getDefaultState(), settings);
    }

    private static DecoratedBumpableBlock decoratedBumpableBlock(RegistryKey<Block> key, AbstractBlock.Settings settings) {
        return decoratedBumpableBlock(key, (BlockState) null, settings);
    }

    private static DecoratedBumpableBlock exclamationBlock(RegistryKey<Block> key, MapColor mapColor) {
        return decoratedBumpableBlock(key, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(mapColor));
    }

    private static NoteBlock noteBlock(RegistryKey<Block> key, MapColor mapColor) {
        return of(key, s -> new NoteBlock(MubbleSounds.NOTE_BLOCK_JUMP_LOW, MubbleSounds.NOTE_BLOCK_JUMP_HIGH, s), AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK).mapColor(mapColor), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    }

    private static NoteBlock marimbaBlock(RegistryKey<Block> key, MapColor mapColor) {
        //TODO: temporary behavior. Change this to the actual Mario Wonder behaviour
        return noteBlock(key, mapColor);
    }

    private static DecoratedBumpableBlock eggBlock(RegistryKey<Block> key, MapColor mapColor) {
        //TODO: change settings
        return decoratedBumpableBlock(key, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(mapColor));
    }
}
