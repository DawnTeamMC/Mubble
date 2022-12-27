package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.*;
import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class SuperMarioContent {
	public static final QuestionBlock QUESTION_BLOCK = new QuestionBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW).item());
	public static final Block EMPTY_BLOCK = new EmptyBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN).item());
	public static final Block BRICK_BLOCK = new Block(DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).item());
  public static final Block GOLD_BLOCK = new Block(DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.YELLOW).item());
	public static final NoteBlock NOTE_BLOCK = new NoteBlock(SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), DawnBlockSettings.copy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.WHITE).item());
	public static final Block BUMPED_BLOCK = new BumpedBlock(DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).strength(-1, 3600000).dropsNothing().nonOpaque());
  public static final SnakeBlock SNAKE_BLOCK = new SnakeBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME).item());
	public static final BeepBlock RED_BEEP_BLOCK = new BeepBlock(DawnBlockSettings.copy(Blocks.GLASS).mapColor(MapColor.RED).item());
	public static final BeepBlockFrame BEEP_BLOCK_FRAME = new BeepBlockFrame(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIGHT_GRAY).nonOpaque().noCollision().item());
	public static final BlockEntityType<BumpedBlockEntity> BUMPED_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
		.create(BumpedBlockEntity::new, BUMPED_BLOCK)
		.build();


	public static void init() {
		Registrar.add(Mubble.id("question_block"), QUESTION_BLOCK);
		Registrar.add(Mubble.id("empty_block"), EMPTY_BLOCK);
		Registrar.add(Mubble.id("brick_block"), BRICK_BLOCK);
		Registrar.add(Mubble.id("gold_block"), GOLD_BLOCK);
		Registrar.add(Mubble.id("note_block"), NOTE_BLOCK);
		Registrar.add(Mubble.id("bumped_block"), BUMPED_BLOCK);
		Registrar.add(Mubble.id("snake_block"), SNAKE_BLOCK);
		Registrar.add(Mubble.id("red_beep_block"), RED_BEEP_BLOCK);
		Registrar.add(Mubble.id("beep_block_frame"), BEEP_BLOCK_FRAME);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, Mubble.id("bumped_block"), BUMPED_BLOCK_ENTITY_TYPE);
	}

	public static final ItemGroup GROUP = FabricItemGroup.builder(Mubble.id("super_mario"))
			.displayName(Text.translatable("item_group.mubble.super_mario"))
			.icon(() -> new ItemStack(SuperMarioContent.QUESTION_BLOCK))
			.entries((enabledFeatures, entries, operatorEnabled) -> {
				entries.add(SuperMarioContent.QUESTION_BLOCK);
				entries.add(SuperMarioContent.EMPTY_BLOCK);
				entries.add(SuperMarioContent.BRICK_BLOCK);
				entries.add(SuperMarioContent.GOLD_BLOCK);
				entries.add(SuperMarioContent.NOTE_BLOCK);
				entries.add(SuperMarioContent.SNAKE_BLOCK);
				entries.add(SuperMarioContent.RED_BEEP_BLOCK);
			})
			.build();
}
