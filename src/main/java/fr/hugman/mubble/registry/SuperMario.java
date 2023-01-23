package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.BeepBlock;
import fr.hugman.mubble.block.EmptyBlock;
import fr.hugman.mubble.block.MarioBumpableBlock;
import fr.hugman.mubble.block.NoteBlock;
import fr.hugman.mubble.block.SnakeBlock;
import fr.hugman.mubble.block.bump.BumpConfig;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.item.CapeFeatherItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

public class SuperMario {
	// BLOCKS
	public static final EmptyBlock EMPTY_BLOCK = new EmptyBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN).item());
	public static final MarioBumpableBlock QUESTION_BLOCK = new MarioBumpableBlock(BumpConfig.DROP_COIN, DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW).item());
	public static final MarioBumpableBlock BRICK_BLOCK = new MarioBumpableBlock(BumpConfig.DESTROY, DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).item());
	public static final MarioBumpableBlock GOLD_BLOCK = new MarioBumpableBlock(BumpConfig.DROP_COIN, DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.YELLOW).item());
	public static final NoteBlock NOTE_BLOCK = new NoteBlock(MubbleSounds.NOTE_BLOCK_JUMP_LOW, MubbleSounds.NOTE_BLOCK_JUMP_HIGH, DawnBlockSettings.copy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.WHITE).item());
	public static final MarioBumpableBlock EXCLAMATION_BLOCK = new MarioBumpableBlock(BumpConfig.NOTHING, DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BLUE).item());
	public static final SnakeBlock SNAKE_BLOCK = new SnakeBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME).item());
	public static final BeepBlock RED_BEEP_BLOCK = new BeepBlock(MapColor.RED, false);
	public static final BeepBlock BLUE_BEEP_BLOCK = new BeepBlock(MapColor.BLUE, true);

	public static final BlockEntityType<BumpableBlockEntity> BUMPABLE_BLOCK_ENTITY_TYPE =
			FabricBlockEntityTypeBuilder.create(BumpableBlockEntity::new, QUESTION_BLOCK, EXCLAMATION_BLOCK, BRICK_BLOCK, GOLD_BLOCK, NOTE_BLOCK).build();

	public static final CapeFeatherItem CAPE_FEATHER = new CapeFeatherItem(new Item.Settings(), false);
	public static final CapeFeatherItem SUPER_CAPE_FEATHER = new CapeFeatherItem(new Item.Settings().rarity(Rarity.EPIC), true);

	public static void init(Registrar r) {
		r.add("empty_block", EMPTY_BLOCK);
		r.add("question_block", QUESTION_BLOCK);
		r.add("brick_block", BRICK_BLOCK);
		r.add("gold_block", GOLD_BLOCK);
		r.add("note_block", NOTE_BLOCK);
		r.add("exclamation_block", EXCLAMATION_BLOCK);
		r.add("snake_block", SNAKE_BLOCK);
		r.add("red_beep_block", RED_BEEP_BLOCK);
		r.add("blue_beep_block", BLUE_BEEP_BLOCK);

		r.add("bumpable_block", BUMPABLE_BLOCK_ENTITY_TYPE);

		r.add("cape_feather", CAPE_FEATHER);
		r.add("super_cape_feather", SUPER_CAPE_FEATHER);
	}

	public static final ItemGroup GROUP = FabricItemGroup.builder(Mubble.id("super_mario"))
			.displayName(Text.translatable("item_group.mubble.super_mario"))
			.icon(() -> new ItemStack(SuperMario.QUESTION_BLOCK))
			.entries((enabledFeatures, entries, operatorEnabled) -> {
				entries.add(SuperMario.QUESTION_BLOCK);
				entries.add(SuperMario.EMPTY_BLOCK);
				entries.add(SuperMario.BRICK_BLOCK);
				entries.add(SuperMario.GOLD_BLOCK);
				entries.add(SuperMario.NOTE_BLOCK);
				entries.add(SuperMario.EXCLAMATION_BLOCK);
				entries.add(SuperMario.SNAKE_BLOCK);
				entries.add(SuperMario.RED_BEEP_BLOCK);
				entries.add(SuperMario.BLUE_BEEP_BLOCK);
				entries.add(SuperMario.CAPE_FEATHER);
				entries.add(SuperMario.SUPER_CAPE_FEATHER);
			})
			.build();
}
