package fr.hugman.mubble.block;

import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
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
    public static final EmptyBlock EMPTY_BLOCK = ofWithItem(MubbleBlockKeys.EMPTY_BLOCK, new EmptyBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN)));
    public static final DecoratedBumpableBlock QUESTION_BLOCK = ofWithItem(MubbleBlockKeys.QUESTION_BLOCK, new DecoratedBumpableBlock(EMPTY_BLOCK.getDefaultState(), AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final DecoratedBumpableBlock BRICK_BLOCK = ofWithItem(MubbleBlockKeys.BRICK_BLOCK, new DecoratedBumpableBlock(Blocks.AIR.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN)),new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final DecoratedBumpableBlock GOLD_BLOCK = ofWithItem(MubbleBlockKeys.GOLD_BLOCK, new DecoratedBumpableBlock(Blocks.AIR.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.GOLD)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final NoteBlock NOTE_BLOCK = ofWithItem(MubbleBlockKeys.NOTE_BLOCK, new NoteBlock(MubbleSounds.NOTE_BLOCK_JUMP_LOW, MubbleSounds.NOTE_BLOCK_JUMP_HIGH, AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.WHITE)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final DecoratedBumpableBlock EXCLAMATION_BLOCK = ofWithItem(MubbleBlockKeys.EXCLAMATION_BLOCK, new DecoratedBumpableBlock(null, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BLUE)), new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT));
    public static final SnakeBlock SNAKE_BLOCK = ofWithItem(MubbleBlockKeys.SNAKE_BLOCK, new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME)));
    public static final SnakeBlock FAST_SNAKE_BLOCK = ofWithItem(MubbleBlockKeys.FAST_SNAKE_BLOCK, new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LAPIS_BLUE)));
    public static final SnakeBlock SLOW_SNAKE_BLOCK = ofWithItem(MubbleBlockKeys.SLOW_SNAKE_BLOCK, new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.RED)));
    public static final BeepBlock RED_BEEP_BLOCK = ofWithItem(MubbleBlockKeys.RED_BEEP_BLOCK, new BeepBlock(BeepBlock.settings(MapColor.RED), false));
    public static final BeepBlock BLUE_BEEP_BLOCK = ofWithItem(MubbleBlockKeys.BLUE_BEEP_BLOCK, new BeepBlock(BeepBlock.settings(MapColor.BLUE), true));

    // SPLATOON
    public static final InkBlock INK_BLOCK = of(MubbleBlockKeys.INK, new InkBlock(AbstractBlock.Settings.create().mapColor(MapColor.WATER_BLUE).replaceable().noCollision().strength(100.0f).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)));

    private static <O extends Block> O of(RegistryKey<Block> key, O block) {
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static <O extends Block> O ofWithItem(RegistryKey<Block> key, O block, Item.Settings settings) {
        of(key, block);
        Registry.register(Registries.ITEM, key.getValue(), new BlockItem(block, settings));
        return block;
    }

    private static <O extends Block> O ofWithItem(RegistryKey<Block> key, O block) {
        return ofWithItem(key, block, new Item.Settings());
    }

}
