package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.*;
import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import fr.hugman.mubble.item.CapeFeatherItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

public class SuperMarioContent {
	public static final QuestionBlock QUESTION_BLOCK = new QuestionBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW).item());
	public static final EmptyBlock EMPTY_BLOCK = new EmptyBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN).item());
	public static final BrickBlock BRICK_BLOCK = new BrickBlock(DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).item());
	public static final SoundEvent BRICK_BLOCK_BREAK = SoundEvent.of(Mubble.id("block.brick_block.break"));
	public static final BrickBlock GOLD_BLOCK = new BrickBlock(DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.YELLOW).item());
	public static final SoundEvent NOTE_BLOCK_JUMP_LOW = SoundEvent.of(Mubble.id("block.note_block.jump.low"));
	public static final SoundEvent NOTE_BLOCK_JUMP_HIGH = SoundEvent.of(Mubble.id("block.note_block.jump.high"));
	public static final NoteBlock NOTE_BLOCK = new NoteBlock(NOTE_BLOCK_JUMP_LOW, NOTE_BLOCK_JUMP_HIGH, DawnBlockSettings.copy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.WHITE).item());
	public static final SnakeBlock SNAKE_BLOCK = new SnakeBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME).item());
	public static final BeepBlock RED_BEEP_BLOCK = new BeepBlock(MapColor.RED, false);
	public static final BeepBlock BLUE_BEEP_BLOCK = new BeepBlock(MapColor.BLUE, true);

	public static final BumpedBlock BUMPED_BLOCK = new BumpedBlock(DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).strength(-1, 3600000).dropsNothing().nonOpaque());
	public static final BlockEntityType<BumpedBlockEntity> BUMPED_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
			.create(BumpedBlockEntity::new, BUMPED_BLOCK)
			.build();
	public static final SoundEvent BUMPABLE_BLOCK_BUMP = SoundEvent.of(Mubble.id("block.bumpable_block.bump"));
	public static final SoundEvent BUMPABLE_BLOCK_CHANGE_LOOT = SoundEvent.of(Mubble.id("block.bumpable_block.change_loot"));
	public static final SoundEvent BUMPABLE_BLOCK_LOOT = SoundEvent.of(Mubble.id("block.bumpable_block.loot"));
	public static final SoundEvent BUMPABLE_BLOCK_LOOT_COIN = SoundEvent.of(Mubble.id("block.bumpable_block.loot_coin"));

	public static final CapeFeatherItem CAPE_FEATHER = new CapeFeatherItem(new Item.Settings(), false);
	public static final CapeFeatherItem SUPER_CAPE_FEATHER = new CapeFeatherItem(new Item.Settings().rarity(Rarity.EPIC), true);
	public static final SoundEvent CAPE_FEATHER_USE = SoundEvent.of(Mubble.id("item.cape_feather.use"));

	public static void init() {
		Registrar.add(Mubble.id("question_block"), QUESTION_BLOCK);
		Registrar.add(Mubble.id("empty_block"), EMPTY_BLOCK);
		Registrar.add(Mubble.id("brick_block"), BRICK_BLOCK);
		Registrar.add(BRICK_BLOCK_BREAK);
		Registrar.add(Mubble.id("gold_block"), GOLD_BLOCK);
		Registrar.add(Mubble.id("note_block"), NOTE_BLOCK);
		Registrar.add(NOTE_BLOCK_JUMP_LOW);
		Registrar.add(NOTE_BLOCK_JUMP_HIGH);
		Registrar.add(Mubble.id("snake_block"), SNAKE_BLOCK);
		Registrar.add(Mubble.id("red_beep_block"), RED_BEEP_BLOCK);
		Registrar.add(Mubble.id("blue_beep_block"), BLUE_BEEP_BLOCK);

		Registrar.add(Mubble.id("bumped_block"), BUMPED_BLOCK);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, Mubble.id("bumped_block"), BUMPED_BLOCK_ENTITY_TYPE);
		Registrar.add(BUMPABLE_BLOCK_CHANGE_LOOT);
		Registrar.add(BUMPABLE_BLOCK_BUMP);
		Registrar.add(BUMPABLE_BLOCK_LOOT);
		Registrar.add(BUMPABLE_BLOCK_LOOT_COIN);

		Registrar.add(Mubble.id("cape_feather"), CAPE_FEATHER);
		Registrar.add(Mubble.id("super_cape_feather"), SUPER_CAPE_FEATHER);
		Registrar.add(CAPE_FEATHER_USE);
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
				entries.add(SuperMarioContent.BLUE_BEEP_BLOCK);
				entries.add(SuperMarioContent.CAPE_FEATHER);
				entries.add(SuperMarioContent.SUPER_CAPE_FEATHER);
			})
			.build();
}
