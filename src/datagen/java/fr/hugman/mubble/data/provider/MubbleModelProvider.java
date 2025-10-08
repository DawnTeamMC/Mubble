package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.data.model.PromenadeModels;
import fr.hugman.mubble.data.texture.MubbleTextureMaps;
import fr.hugman.mubble.item.MubbleItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;

public class MubbleModelProvider extends FabricModelProvider {
	public MubbleModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator gen) {
		// SUPER MARIO
		gen.registerSingleton(MubbleBlocks.BRICK_BLOCK, TexturedModel.makeFactory(block -> TextureMap.all(Mubble.id("block/brick_block_brown")), Models.CUBE_ALL));

		// YOSHI'S ISLAND
		gen.registerSingleton(MubbleBlocks.BLUE_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "blue"), PromenadeModels.EGG_BLOCK));
		gen.registerSingleton(MubbleBlocks.CYAN_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "cyan"), PromenadeModels.EGG_BLOCK));
		gen.registerSingleton(MubbleBlocks.GREEN_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "green"), PromenadeModels.EGG_BLOCK));
		gen.registerSingleton(MubbleBlocks.YELLOW_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "yellow"), PromenadeModels.EGG_BLOCK));
		gen.registerSingleton(MubbleBlocks.ORANGE_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "orange"), PromenadeModels.EGG_BLOCK));
		gen.registerSingleton(MubbleBlocks.RED_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "red"), PromenadeModels.EGG_BLOCK));
		gen.registerSingleton(MubbleBlocks.PINK_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "magenta"), PromenadeModels.EGG_BLOCK));
		gen.registerSingleton(MubbleBlocks.BLACK_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "black"), PromenadeModels.EGG_BLOCK));
		gen.registerSingleton(MubbleBlocks.WHITE_EGG_BLOCK, TexturedModel.makeFactory(block -> MubbleTextureMaps.palettedSideEnd(Mubble.id("egg_block"), "white"), PromenadeModels.EGG_BLOCK));
	}

	@Override
	public void generateItemModels(ItemModelGenerator gen) {
		// SUPER MARIO
		gen.register(MubbleItems.MAKER_GLOVE, Models.GENERATED);
		gen.register(MubbleItems.CAPE_FEATHER, Models.GENERATED);
		gen.register(MubbleItems.SUPER_CAPE_FEATHER, Models.GENERATED);
		gen.register(MubbleItems.GOOMBA_SPAWN_EGG, Models.GENERATED);
	}
}
