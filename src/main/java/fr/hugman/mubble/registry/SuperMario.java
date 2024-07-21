package fr.hugman.mubble.registry;

import fr.hugman.dawn.Registrar;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.BeepBlock;
import fr.hugman.mubble.block.DecoratedBumpableBlock;
import fr.hugman.mubble.block.EmptyBlock;
import fr.hugman.mubble.block.NoteBlock;
import fr.hugman.mubble.block.SnakeBlock;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.item.CapeFeatherItem;
import fr.hugman.mubble.screen.BumpableScreenHandler;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

public class SuperMario {
	// ITEMS
	public static final Item MAKER_GLOVE = new Item(new Item.Settings().maxCount(1));

	// BLOCKS
	public static final EmptyBlock EMPTY_BLOCK = new EmptyBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BROWN).item());
	public static final DecoratedBumpableBlock QUESTION_BLOCK = new DecoratedBumpableBlock(EMPTY_BLOCK.getDefaultState(), AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.YELLOW).item(new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)));
	public static final DecoratedBumpableBlock BRICK_BLOCK = new DecoratedBumpableBlock(Blocks.AIR.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN).item(new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)));
	public static final DecoratedBumpableBlock GOLD_BLOCK = new DecoratedBumpableBlock(Blocks.AIR.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.GOLD).item(new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)));
	public static final NoteBlock NOTE_BLOCK = new NoteBlock(MubbleSounds.NOTE_BLOCK_JUMP_LOW, MubbleSounds.NOTE_BLOCK_JUMP_HIGH, AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.WHITE).item(new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)));
	public static final DecoratedBumpableBlock EXCLAMATION_BLOCK = new DecoratedBumpableBlock(null, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BLUE).item(new Item.Settings().component(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT)));
	public static final SnakeBlock SNAKE_BLOCK = new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LIME).item());
	public static final SnakeBlock FAST_SNAKE_BLOCK = new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.LAPIS_BLUE).item());
	public static final SnakeBlock SLOW_SNAKE_BLOCK = new SnakeBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.RED).item());
	public static final BeepBlock RED_BEEP_BLOCK = new BeepBlock(MapColor.RED, false);
	public static final BeepBlock BLUE_BEEP_BLOCK = new BeepBlock(MapColor.BLUE, true);

	public static final ScreenHandlerType<BumpableScreenHandler> BUMPABLE_BLOCK_SCREEN_HANDLER = new ScreenHandlerType<>(BumpableScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static final BlockEntityType<BumpableBlockEntity> BUMPABLE_BLOCK_ENTITY_TYPE =
			FabricBlockEntityTypeBuilder.create(BumpableBlockEntity::new, QUESTION_BLOCK, BRICK_BLOCK, GOLD_BLOCK, NOTE_BLOCK, EXCLAMATION_BLOCK).build();

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
