package fr.hugman.mubble.registry;

import fr.hugman.dawn.DawnFactory;
import fr.hugman.dawn.Registrar;
import fr.hugman.dawn.block.DawnBlockSettings;
import fr.hugman.dawn.item.DawnItemSettings;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.*;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.block.entity.WarpBlockEntity;
import fr.hugman.mubble.item.CapeFeatherItem;
import fr.hugman.mubble.screen.BumpableBlockScreenHandler;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

public class SuperMario {
	// ITEMS
	public static final Item MAKER_GLOVE = new Item(new DawnItemSettings().maxCount(1));

	// BLOCKS
	public static final EmptyBlock EMPTY_BLOCK = new EmptyBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN).item());
	public static final DecoratedBumpableBlock QUESTION_BLOCK = new DecoratedBumpableBlock(new ItemStack(Items.GOLD_NUGGET), EMPTY_BLOCK.getDefaultState(), DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW).item());
	public static final DecoratedBumpableBlock BRICK_BLOCK = new DecoratedBumpableBlock(ItemStack.EMPTY, Blocks.AIR.getDefaultState(), DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).item());
	public static final DecoratedBumpableBlock GOLD_BLOCK = new DecoratedBumpableBlock(new ItemStack(Items.GOLD_NUGGET), Blocks.AIR.getDefaultState(), DawnBlockSettings.copy(Blocks.BRICKS).mapColor(MapColor.GOLD).item());
	public static final NoteBlock NOTE_BLOCK = new NoteBlock(MubbleSounds.NOTE_BLOCK_JUMP_LOW, MubbleSounds.NOTE_BLOCK_JUMP_HIGH, DawnBlockSettings.copy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.WHITE).item());
	public static final DecoratedBumpableBlock EXCLAMATION_BLOCK = new DecoratedBumpableBlock(ItemStack.EMPTY, null, DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BLUE).item());
	public static final SnakeBlock SNAKE_BLOCK = new SnakeBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME).item());
	public static final SnakeBlock FAST_SNAKE_BLOCK = new SnakeBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LAPIS_BLUE).item());
	public static final SnakeBlock SLOW_SNAKE_BLOCK = new SnakeBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.RED).item());
	public static final BeepBlock RED_BEEP_BLOCK = new BeepBlock(MapColor.RED, false);
	public static final BeepBlock BLUE_BEEP_BLOCK = new BeepBlock(MapColor.BLUE, true);
	public static final WarpBlock WARP_PIPE = new WarpBlock(DawnBlockSettings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.GREEN).item());
	public static final ScreenHandlerType<BumpableBlockScreenHandler> WARP_BLOCK_SCREEN_HANDLER = new ScreenHandlerType<>(BumpableBlockScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static final ScreenHandlerType<BumpableBlockScreenHandler> BUMPABLE_BLOCK_SCREEN_HANDLER = new ScreenHandlerType<>(BumpableBlockScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static final BlockEntityType<BumpableBlockEntity> BUMPABLE_BLOCK_ENTITY_TYPE =
			FabricBlockEntityTypeBuilder.create(BumpableBlockEntity::new, QUESTION_BLOCK, BRICK_BLOCK, GOLD_BLOCK, NOTE_BLOCK, EXCLAMATION_BLOCK).build();
	public static final BlockEntityType<WarpBlockEntity> WARP_BLOCK_ENTITY_TYPE =
			FabricBlockEntityTypeBuilder.create(WarpBlockEntity::new, WARP_PIPE).build();
	public static final TagKey<Item> CAN_OPEN_BUMPABLE_BLOCKS = DawnFactory.itemTag(Mubble.id("can_open_bumpable_blocks"));

	public static final CapeFeatherItem CAPE_FEATHER = new CapeFeatherItem(new Item.Settings(), false);
	public static final CapeFeatherItem SUPER_CAPE_FEATHER = new CapeFeatherItem(new Item.Settings().rarity(Rarity.EPIC), true);

	// ITEM GROUP
	public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Mubble.id("super_mario"));

	public static void init(Registrar r) {
		r.add("maker_glove", MAKER_GLOVE);

		r.add("empty_block", EMPTY_BLOCK);
		r.add("question_block", QUESTION_BLOCK);
		r.add("brick_block", BRICK_BLOCK);
		r.add("gold_block", GOLD_BLOCK);
		r.add("note_block", NOTE_BLOCK);
		r.add("exclamation_block", EXCLAMATION_BLOCK);
		r.add("snake_block", SNAKE_BLOCK);
		r.add("fast_snake_block", FAST_SNAKE_BLOCK);
		r.add("slow_snake_block", SLOW_SNAKE_BLOCK);
		r.add("red_beep_block", RED_BEEP_BLOCK);
		r.add("blue_beep_block", BLUE_BEEP_BLOCK);
		r.add("warp_pipe", WARP_PIPE);

		Registry.register(Registries.SCREEN_HANDLER, r.id("warp_block"), WARP_BLOCK_SCREEN_HANDLER);
		r.add("warp_block", WARP_BLOCK_ENTITY_TYPE);

		Registry.register(Registries.SCREEN_HANDLER, r.id("bumpable_block"), BUMPABLE_BLOCK_SCREEN_HANDLER); //TODO: create a registrar method for screen handlers in Dawn API
		r.add("bumpable_block", BUMPABLE_BLOCK_ENTITY_TYPE);

		r.add("cape_feather", CAPE_FEATHER);
		r.add("super_cape_feather", SUPER_CAPE_FEATHER);

		appendItemGroups();
	}

	public static void appendItemGroups() {
		Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
				.displayName(Text.translatable("item_group.mubble.super_mario"))
				.icon(() -> new ItemStack(SuperMario.QUESTION_BLOCK))
				.entries((displayContext, entries) -> {
					entries.add(SuperMario.MAKER_GLOVE);
					entries.add(SuperMario.QUESTION_BLOCK);
					entries.add(SuperMario.EMPTY_BLOCK);
					entries.add(SuperMario.BRICK_BLOCK);
					entries.add(SuperMario.GOLD_BLOCK);
					entries.add(SuperMario.NOTE_BLOCK);
					entries.add(SuperMario.EXCLAMATION_BLOCK);
					entries.add(SuperMario.SNAKE_BLOCK);
					entries.add(SuperMario.FAST_SNAKE_BLOCK);
					entries.add(SuperMario.SLOW_SNAKE_BLOCK);
					entries.add(SuperMario.RED_BEEP_BLOCK);
					entries.add(SuperMario.BLUE_BEEP_BLOCK);
					entries.add(SuperMario.CAPE_FEATHER);
					entries.add(SuperMario.SUPER_CAPE_FEATHER);
				})
				.build());
	}
}
