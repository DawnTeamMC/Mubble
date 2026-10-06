package fr.hugman.mubble.super_mario.world.level.block;

import fr.hugman.mubble.super_mario.references.SuperMarioBlockItemIds;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SuperMarioBlocks {
    public static final EmptyBlock EMPTY_BLOCK = register(SuperMarioBlockItemIds.EMPTY_BLOCK, EmptyBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BROWN));
    public static final DecoratedBumpableBlock QUESTION_BLOCK = registerBumpableBlock(SuperMarioBlockItemIds.QUESTION_BLOCK, EMPTY_BLOCK, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_YELLOW));
    public static final DecoratedBumpableBlock BRICK_BLOCK = registerBumpableBlock(SuperMarioBlockItemIds.BRICK_BLOCK, Blocks.AIR, BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).explosionResistance(0).mapColor(MapColor.COLOR_BROWN));
    public static final DecoratedBumpableBlock CRYSTAL_BLOCK = registerBumpableBlock(SuperMarioBlockItemIds.CRYSTAL_BLOCK, Blocks.AIR, BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).explosionResistance(0).sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_PURPLE));
    public static final DecoratedBumpableBlock GOLD_BLOCK = registerBumpableBlock(SuperMarioBlockItemIds.GOLD_BLOCK, Blocks.AIR, BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.GOLD));
    public static final DecoratedBumpableBlock BLUE_EXCLAMATION_BLOCK = registerExclamationBlock(SuperMarioBlockItemIds.BLUE_EXCLAMATION_BLOCK, MapColor.COLOR_BLUE);
    public static final DecoratedBumpableBlock GREEN_EXCLAMATION_BLOCK = registerExclamationBlock(SuperMarioBlockItemIds.GREEN_EXCLAMATION_BLOCK, MapColor.COLOR_GREEN);
    public static final DecoratedBumpableBlock YELLOW_EXCLAMATION_BLOCK = registerExclamationBlock(SuperMarioBlockItemIds.YELLOW_EXCLAMATION_BLOCK, MapColor.COLOR_YELLOW);
    public static final DecoratedBumpableBlock RED_EXCLAMATION_BLOCK = registerExclamationBlock(SuperMarioBlockItemIds.RED_EXCLAMATION_BLOCK, MapColor.COLOR_RED);
    public static final NoteBlock NOTE_BLOCK = registerNoteBlock(SuperMarioBlockItemIds.NOTE_BLOCK, MapColor.SNOW);
    public static final NoteBlock BLUE_MARIMBA_BLOCK = registerMarimbaBlock(SuperMarioBlockItemIds.BLUE_MARIMBA_BLOCK, MapColor.COLOR_BLUE);
    public static final NoteBlock GREEN_MARIMBA_BLOCK = registerMarimbaBlock(SuperMarioBlockItemIds.GREEN_MARIMBA_BLOCK, MapColor.COLOR_GREEN);
    public static final NoteBlock YELLOW_MARIMBA_BLOCK = registerMarimbaBlock(SuperMarioBlockItemIds.YELLOW_MARIMBA_BLOCK, MapColor.COLOR_YELLOW);
    public static final NoteBlock RED_MARIMBA_BLOCK = registerMarimbaBlock(SuperMarioBlockItemIds.RED_MARIMBA_BLOCK, MapColor.COLOR_RED);
    public static final SnakeBlock SNAKE_BLOCK = register(SuperMarioBlockItemIds.SNAKE_BLOCK, SnakeBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_LIGHT_GREEN));
    public static final SnakeBlock FAST_SNAKE_BLOCK = register(SuperMarioBlockItemIds.FAST_SNAKE_BLOCK, SnakeBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).mapColor(MapColor.LAPIS));
    public static final SnakeBlock SLOW_SNAKE_BLOCK = register(SuperMarioBlockItemIds.SLOW_SNAKE_BLOCK, SnakeBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_RED));
    public static final BeepBlock RED_BEEP_BLOCK = register(SuperMarioBlockItemIds.RED_BEEP_BLOCK, s -> new BeepBlock(s, false), BeepBlock.settings(MapColor.COLOR_RED));
    public static final BeepBlock BLUE_BEEP_BLOCK = register(SuperMarioBlockItemIds.BLUE_BEEP_BLOCK, s -> new BeepBlock(s, true), BeepBlock.settings(MapColor.COLOR_BLUE));

    public static final DecoratedBumpableBlock BLUE_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.BLUE_EGG_BLOCK, MapColor.COLOR_BLUE);
    public static final DecoratedBumpableBlock CYAN_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.CYAN_EGG_BLOCK, MapColor.COLOR_CYAN);
    public static final DecoratedBumpableBlock GREEN_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.GREEN_EGG_BLOCK, MapColor.COLOR_GREEN);
    public static final DecoratedBumpableBlock YELLOW_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.YELLOW_EGG_BLOCK, MapColor.COLOR_YELLOW);
    public static final DecoratedBumpableBlock ORANGE_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.ORANGE_EGG_BLOCK, MapColor.COLOR_ORANGE);
    public static final DecoratedBumpableBlock RED_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.RED_EGG_BLOCK, MapColor.COLOR_RED);
    public static final DecoratedBumpableBlock PINK_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.PINK_EGG_BLOCK, MapColor.COLOR_PINK);
    public static final DecoratedBumpableBlock BLACK_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.BLACK_EGG_BLOCK, MapColor.COLOR_BLACK);
    public static final DecoratedBumpableBlock WHITE_EGG_BLOCK = registerEggBlock(SuperMarioBlockItemIds.WHITE_EGG_BLOCK, MapColor.SNOW);

    private static <B extends Block> B noItem(BlockItemId id, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties blockSettings) {
        B block = factory.apply(blockSettings.setId(id.block()));
        return Registry.register(BuiltInRegistries.BLOCK, id.block(), block);
    }

    private static <B extends Block> B register(BlockItemId id, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties blockSettings, Item.Properties itemSettings) {
        B block = noItem(id, factory, blockSettings);
        Registry.register(BuiltInRegistries.ITEM, id.item(), new BlockItem(block, itemSettings.setId(id.item()).useBlockDescriptionPrefix()));
        return block;
    }

    private static <O extends Block> O register(BlockItemId key, Function<BlockBehaviour.Properties, O> factory, BlockBehaviour.Properties settings) {
        return register(key, factory, settings, new Item.Properties());
    }

    private static DecoratedBumpableBlock registerBumpableBlock(BlockItemId key, @Nullable BlockState defaultBumpedState, BlockBehaviour.Properties settings) {
        return register(key, s -> new DecoratedBumpableBlock(defaultBumpedState, s), settings, new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    }

    private static DecoratedBumpableBlock registerBumpableBlock(BlockItemId key, Block defaultBumpedBlock, BlockBehaviour.Properties settings) {
        return registerBumpableBlock(key, defaultBumpedBlock.defaultBlockState(), settings);
    }

    private static DecoratedBumpableBlock registerBumpableBlock(BlockItemId key, BlockBehaviour.Properties settings) {
        return registerBumpableBlock(key, (BlockState) null, settings);
    }

    private static DecoratedBumpableBlock registerExclamationBlock(BlockItemId key, MapColor mapColor) {
        return registerBumpableBlock(key, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).mapColor(mapColor));
    }

    private static NoteBlock registerNoteBlock(BlockItemId key, MapColor mapColor) {
        return register(key, s -> new NoteBlock(SuperMarioSounds.NOTE_BLOCK_JUMP_LOW, SuperMarioSounds.NOTE_BLOCK_JUMP_HIGH, s), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(mapColor), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    }

    private static NoteBlock registerMarimbaBlock(BlockItemId key, MapColor mapColor) {
        //TODO: temporary behavior. Change this to the actual Mario Wonder behaviour
        return registerNoteBlock(key, mapColor);
    }

    private static DecoratedBumpableBlock registerEggBlock(BlockItemId key, MapColor mapColor) {
        //TODO: change sounds
        return registerBumpableBlock(key, BlockBehaviour.Properties.ofFullCopy(Blocks.BONE_BLOCK).mapColor(mapColor));
    }
}
