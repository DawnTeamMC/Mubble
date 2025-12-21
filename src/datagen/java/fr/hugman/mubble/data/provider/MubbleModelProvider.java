package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.data.model.MubbleTexturedModels;
import fr.hugman.mubble.data.model.MubbleTextureMaps;
import fr.hugman.mubble.world.item.MubbleItems;
import fr.hugman.mubble.world.level.block.BeepBlock;
import fr.hugman.mubble.world.level.block.MubbleBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.client.data.models.BlockModelGenerators.createBooleanModelDispatch;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class MubbleModelProvider extends FabricModelProvider {
	private final CompletableFuture<HolderLookup.Provider> registriesFuture;

	public MubbleModelProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output);
		this.registriesFuture = registriesFuture;
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators gen) {
		// SUPER MARIO
		gen.createTrivialBlock(MubbleBlocks.EMPTY_BLOCK, TexturedModel.createDefault(block -> MubbleTextureMaps.all(Mubble.id("bolted_block/normal_brown")), ModelTemplates.CUBE_ALL));
		gen.createTrivialBlock(MubbleBlocks.QUESTION_BLOCK, TexturedModel.createDefault(block -> MubbleTextureMaps.sideEnd(Mubble.id("bolted_block/question_yellow"), Mubble.id("bolted_block/normal_yellow")), ModelTemplates.CUBE_COLUMN));
		gen.createTrivialBlock(MubbleBlocks.BRICK_BLOCK, MubbleTexturedModels.brickBlock("brown"));
		gen.createTrivialBlock(MubbleBlocks.CRYSTAL_BLOCK, MubbleTexturedModels.brickBlock("amethyst"));
		gen.createTrivialBlock(MubbleBlocks.GOLD_BLOCK, MubbleTexturedModels.brickBlock("gold"));
		gen.createTrivialBlock(MubbleBlocks.BLUE_EXCLAMATION_BLOCK, MubbleTexturedModels.exclamationBlock("blue"));
		gen.createTrivialBlock(MubbleBlocks.GREEN_EXCLAMATION_BLOCK, MubbleTexturedModels.exclamationBlock("green"));
		gen.createTrivialBlock(MubbleBlocks.YELLOW_EXCLAMATION_BLOCK, MubbleTexturedModels.exclamationBlock("yellow"));
		gen.createTrivialBlock(MubbleBlocks.RED_EXCLAMATION_BLOCK, MubbleTexturedModels.exclamationBlock("red"));
		gen.createTrivialBlock(MubbleBlocks.NOTE_BLOCK, TexturedModel.createDefault(block -> MubbleTextureMaps.sideEnd(Mubble.id("smooth_block/note_white"), Mubble.id("smooth_block/straight_white")), ModelTemplates.CUBE_COLUMN));
		gen.createTrivialBlock(MubbleBlocks.BLUE_MARIMBA_BLOCK, MubbleTexturedModels.marimbaBlock("blue"));
		gen.createTrivialBlock(MubbleBlocks.GREEN_MARIMBA_BLOCK, MubbleTexturedModels.marimbaBlock("green"));
		gen.createTrivialBlock(MubbleBlocks.YELLOW_MARIMBA_BLOCK, MubbleTexturedModels.marimbaBlock("yellow"));
		gen.createTrivialBlock(MubbleBlocks.RED_MARIMBA_BLOCK, MubbleTexturedModels.marimbaBlock("red"));
		gen.createTrivialBlock(MubbleBlocks.SNAKE_BLOCK, MubbleTexturedModels.snakeBlock("normal"));
		gen.createTrivialBlock(MubbleBlocks.FAST_SNAKE_BLOCK, MubbleTexturedModels.snakeBlock("fast"));
		gen.createTrivialBlock(MubbleBlocks.SLOW_SNAKE_BLOCK, MubbleTexturedModels.snakeBlock("slow"));
		registerBeepBlock(gen, MubbleBlocks.RED_BEEP_BLOCK, "red");
		registerBeepBlock(gen, MubbleBlocks.BLUE_BEEP_BLOCK, "blue");

		// YOSHI'S ISLAND
		gen.createTrivialBlock(MubbleBlocks.BLUE_EGG_BLOCK, MubbleTexturedModels.eggBlock("blue"));
		gen.createTrivialBlock(MubbleBlocks.CYAN_EGG_BLOCK, MubbleTexturedModels.eggBlock("cyan"));
		gen.createTrivialBlock(MubbleBlocks.GREEN_EGG_BLOCK, MubbleTexturedModels.eggBlock("green"));
		gen.createTrivialBlock(MubbleBlocks.YELLOW_EGG_BLOCK, MubbleTexturedModels.eggBlock("yellow"));
		gen.createTrivialBlock(MubbleBlocks.ORANGE_EGG_BLOCK, MubbleTexturedModels.eggBlock("orange"));
		gen.createTrivialBlock(MubbleBlocks.RED_EGG_BLOCK, MubbleTexturedModels.eggBlock("red"));
		gen.createTrivialBlock(MubbleBlocks.PINK_EGG_BLOCK, MubbleTexturedModels.eggBlock("magenta"));
		gen.createTrivialBlock(MubbleBlocks.BLACK_EGG_BLOCK, MubbleTexturedModels.eggBlock("black"));
		gen.createTrivialBlock(MubbleBlocks.WHITE_EGG_BLOCK, MubbleTexturedModels.eggBlock("white"));
	}

	@Override
	public void generateItemModels(ItemModelGenerators gen) {
		// SUPER MARIO
		gen.generateFlatItem(MubbleItems.MAKER_GLOVE, ModelTemplates.FLAT_ITEM);

		gen.generateFlatItem(MubbleItems.GREEN_KOOPA_SHELL, ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(MubbleItems.RED_KOOPA_SHELL, ModelTemplates.FLAT_ITEM);

		gen.generateFlatItem(MubbleItems.MINI_MUSHROOM, ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(MubbleItems.MEGA_MUSHROOM, ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(MubbleItems.FIRE_FLOWER, ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(MubbleItems.ICE_FLOWER, ModelTemplates.FLAT_ITEM);

		gen.generateFlatItem(MubbleItems.CAPE_FEATHER, ModelTemplates.FLAT_ITEM);
		gen.generateFlatItem(MubbleItems.SUPER_CAPE_FEATHER, ModelTemplates.FLAT_ITEM);

		gen.generateFlatItem(MubbleItems.GOOMBA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
	}

	private static void registerBeepBlock(BlockModelGenerators gen, Block block, String color) {
		MultiVariant normal = plainVariant(MubbleTexturedModels.beepBlock(color).create(block, gen.modelOutput));
		MultiVariant frame = plainVariant(Mubble.id("block/beep_block/frame"));
		gen.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(createBooleanModelDispatch(BeepBlock.FRAME, frame, normal)));
	}

	private void registerGoombaVariantSpawnEggs(ItemModelGenerators gen, Item item) {
		//TODO figure this out
		ItemModel.Unbaked unbaked = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));

		var miniId = Mubble.id("item/mini_goomba_spawn_egg");
		ItemModel.Unbaked mini = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(miniId,
				TextureMapping.layer0(miniId),
				gen.modelOutput
		));

		/*
		this.registriesFuture.thenAccept(registries -> {
			var variant = registries.getOrThrow(MubbleRegistryKeys.GOOMBA_VARIANT).getOrThrow(GoombaVariants.MINI);
			SelectItemModel.SwitchCase<RegistryEntry<GoombaVariant>> switchCase = ItemModels.switchCase(variant, mini);
			ItemModel.Unbaked model = ItemModels.select(
					new ComponentSelectProperty<>(MubbleDataComponentTypes.GOOMBA_VARIANT),
					unbaked,
					switchCase
			);
			gen.output.accept(item, model);
		});:
		 */
		gen.itemModelOutput.accept(item, unbaked);
	}
}
