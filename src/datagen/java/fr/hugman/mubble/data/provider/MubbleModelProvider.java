package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.BeepBlock;
import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.data.model.MubbleTexturedModels;
import fr.hugman.mubble.data.model.MubbleTextureMaps;
import fr.hugman.mubble.item.MubbleItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.WeightedVariant;

import static net.minecraft.client.data.BlockStateModelGenerator.createBooleanModelMap;
import static net.minecraft.client.data.BlockStateModelGenerator.createWeightedVariant;

public class MubbleModelProvider extends FabricModelProvider {
	public MubbleModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator gen) {
		// SUPER MARIO
		gen.registerSingleton(MubbleBlocks.EMPTY_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.all(Mubble.id("bolted_block/normal_brown")), Models.CUBE_ALL));
		gen.registerSingleton(MubbleBlocks.QUESTION_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.sideEnd(Mubble.id("bolted_block/question_yellow"), Mubble.id("bolted_block/normal_yellow")), Models.CUBE_COLUMN));
		gen.registerSingleton(MubbleBlocks.BRICK_BLOCK, MubbleTexturedModels.brickBlock("brown"));
		gen.registerSingleton(MubbleBlocks.CRYSTAL_BLOCK, MubbleTexturedModels.brickBlock("amethyst"));
		gen.registerSingleton(MubbleBlocks.GOLD_BLOCK, MubbleTexturedModels.brickBlock("gold"));
		gen.registerSingleton(MubbleBlocks.BLUE_EXCLAMATION_BLOCK, MubbleTexturedModels.exclamationBlock("blue"));
		gen.registerSingleton(MubbleBlocks.GREEN_EXCLAMATION_BLOCK, MubbleTexturedModels.exclamationBlock("green"));
		gen.registerSingleton(MubbleBlocks.YELLOW_EXCLAMATION_BLOCK, MubbleTexturedModels.exclamationBlock("yellow"));
		gen.registerSingleton(MubbleBlocks.RED_EXCLAMATION_BLOCK, MubbleTexturedModels.exclamationBlock("red"));
		gen.registerSingleton(MubbleBlocks.NOTE_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.sideEnd(Mubble.id("smooth_block/note_white"), Mubble.id("smooth_block/straight_white")), Models.CUBE_COLUMN));
		gen.registerSingleton(MubbleBlocks.BLUE_MARIMBA_BLOCK, MubbleTexturedModels.marimbaBlock("blue"));
		gen.registerSingleton(MubbleBlocks.GREEN_MARIMBA_BLOCK, MubbleTexturedModels.marimbaBlock("green"));
		gen.registerSingleton(MubbleBlocks.YELLOW_MARIMBA_BLOCK, MubbleTexturedModels.marimbaBlock("yellow"));
		gen.registerSingleton(MubbleBlocks.RED_MARIMBA_BLOCK, MubbleTexturedModels.marimbaBlock("red"));
		gen.registerSingleton(MubbleBlocks.SNAKE_BLOCK, MubbleTexturedModels.snakeBlock("normal"));
		gen.registerSingleton(MubbleBlocks.FAST_SNAKE_BLOCK, MubbleTexturedModels.snakeBlock("fast"));
		gen.registerSingleton(MubbleBlocks.SLOW_SNAKE_BLOCK, MubbleTexturedModels.snakeBlock("slow"));
		registerBeepBlock(gen, MubbleBlocks.RED_BEEP_BLOCK, "red");
		registerBeepBlock(gen, MubbleBlocks.BLUE_BEEP_BLOCK, "blue");

		// YOSHI'S ISLAND
		gen.registerSingleton(MubbleBlocks.BLUE_EGG_BLOCK, MubbleTexturedModels.eggBlock("blue"));
		gen.registerSingleton(MubbleBlocks.CYAN_EGG_BLOCK, MubbleTexturedModels.eggBlock("cyan"));
		gen.registerSingleton(MubbleBlocks.GREEN_EGG_BLOCK, MubbleTexturedModels.eggBlock("green"));
		gen.registerSingleton(MubbleBlocks.YELLOW_EGG_BLOCK, MubbleTexturedModels.eggBlock("yellow"));
		gen.registerSingleton(MubbleBlocks.ORANGE_EGG_BLOCK, MubbleTexturedModels.eggBlock("orange"));
		gen.registerSingleton(MubbleBlocks.RED_EGG_BLOCK, MubbleTexturedModels.eggBlock("red"));
		gen.registerSingleton(MubbleBlocks.PINK_EGG_BLOCK, MubbleTexturedModels.eggBlock("magenta"));
		gen.registerSingleton(MubbleBlocks.BLACK_EGG_BLOCK, MubbleTexturedModels.eggBlock("black"));
		gen.registerSingleton(MubbleBlocks.WHITE_EGG_BLOCK, MubbleTexturedModels.eggBlock("white"));
	}

	@Override
	public void generateItemModels(ItemModelGenerator gen) {
		// SUPER MARIO
		gen.register(MubbleItems.MAKER_GLOVE, Models.GENERATED);
		gen.register(MubbleItems.CAPE_FEATHER, Models.GENERATED);
		gen.register(MubbleItems.SUPER_CAPE_FEATHER, Models.GENERATED);
		gen.register(MubbleItems.GOOMBA_SPAWN_EGG, Models.GENERATED);
	}

	private static void registerBeepBlock(BlockStateModelGenerator gen, Block block, String color) {
		WeightedVariant normal = createWeightedVariant(MubbleTexturedModels.beepBlock(color).upload(block, gen.modelCollector));
		WeightedVariant frame = createWeightedVariant(Mubble.id("block/beep_block/frame"));
		gen.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block).with(createBooleanModelMap(BeepBlock.FRAME, frame, normal)));
	}
}
