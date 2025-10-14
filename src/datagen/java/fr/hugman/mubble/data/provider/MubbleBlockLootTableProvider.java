package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.block.MubbleBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class MubbleBlockLootTableProvider extends FabricBlockLootTableProvider {
	public MubbleBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		addDrop(MubbleBlocks.EMPTY_BLOCK);
		addDrop(MubbleBlocks.QUESTION_BLOCK);
		addDrop(MubbleBlocks.BRICK_BLOCK);
		addDrop(MubbleBlocks.CRYSTAL_BLOCK);
		addDrop(MubbleBlocks.GOLD_BLOCK);
		addDrop(MubbleBlocks.BLUE_EXCLAMATION_BLOCK);
		addDrop(MubbleBlocks.GREEN_EXCLAMATION_BLOCK);
		addDrop(MubbleBlocks.YELLOW_EXCLAMATION_BLOCK);
		addDrop(MubbleBlocks.RED_EXCLAMATION_BLOCK);
		addDrop(MubbleBlocks.NOTE_BLOCK);
		addDrop(MubbleBlocks.BLUE_MARIMBA_BLOCK);
		addDrop(MubbleBlocks.GREEN_MARIMBA_BLOCK);
		addDrop(MubbleBlocks.YELLOW_MARIMBA_BLOCK);
		addDrop(MubbleBlocks.RED_MARIMBA_BLOCK);
		addDrop(MubbleBlocks.SNAKE_BLOCK);
		addDrop(MubbleBlocks.FAST_SNAKE_BLOCK);
		addDrop(MubbleBlocks.SLOW_SNAKE_BLOCK);
		addDrop(MubbleBlocks.RED_BEEP_BLOCK);
		addDrop(MubbleBlocks.BLUE_BEEP_BLOCK);

		addDrop(MubbleBlocks.BLUE_EGG_BLOCK);
		addDrop(MubbleBlocks.CYAN_EGG_BLOCK);
		addDrop(MubbleBlocks.GREEN_EGG_BLOCK);
		addDrop(MubbleBlocks.YELLOW_EGG_BLOCK);
		addDrop(MubbleBlocks.ORANGE_EGG_BLOCK);
		addDrop(MubbleBlocks.RED_EGG_BLOCK);
		addDrop(MubbleBlocks.PINK_EGG_BLOCK);
		addDrop(MubbleBlocks.BLACK_EGG_BLOCK);
		addDrop(MubbleBlocks.WHITE_EGG_BLOCK);
	}
}