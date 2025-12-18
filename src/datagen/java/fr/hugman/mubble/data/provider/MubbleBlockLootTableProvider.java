package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.block.MubbleBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class MubbleBlockLootTableProvider extends FabricBlockLootTableProvider {
	public MubbleBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		dropSelf(MubbleBlocks.EMPTY_BLOCK);
		dropSelf(MubbleBlocks.QUESTION_BLOCK);
		dropSelf(MubbleBlocks.BRICK_BLOCK);
		dropSelf(MubbleBlocks.CRYSTAL_BLOCK);
		dropSelf(MubbleBlocks.GOLD_BLOCK);
		dropSelf(MubbleBlocks.BLUE_EXCLAMATION_BLOCK);
		dropSelf(MubbleBlocks.GREEN_EXCLAMATION_BLOCK);
		dropSelf(MubbleBlocks.YELLOW_EXCLAMATION_BLOCK);
		dropSelf(MubbleBlocks.RED_EXCLAMATION_BLOCK);
		dropSelf(MubbleBlocks.NOTE_BLOCK);
		dropSelf(MubbleBlocks.BLUE_MARIMBA_BLOCK);
		dropSelf(MubbleBlocks.GREEN_MARIMBA_BLOCK);
		dropSelf(MubbleBlocks.YELLOW_MARIMBA_BLOCK);
		dropSelf(MubbleBlocks.RED_MARIMBA_BLOCK);
		dropSelf(MubbleBlocks.SNAKE_BLOCK);
		dropSelf(MubbleBlocks.FAST_SNAKE_BLOCK);
		dropSelf(MubbleBlocks.SLOW_SNAKE_BLOCK);
		dropSelf(MubbleBlocks.RED_BEEP_BLOCK);
		dropSelf(MubbleBlocks.BLUE_BEEP_BLOCK);

		dropSelf(MubbleBlocks.BLUE_EGG_BLOCK);
		dropSelf(MubbleBlocks.CYAN_EGG_BLOCK);
		dropSelf(MubbleBlocks.GREEN_EGG_BLOCK);
		dropSelf(MubbleBlocks.YELLOW_EGG_BLOCK);
		dropSelf(MubbleBlocks.ORANGE_EGG_BLOCK);
		dropSelf(MubbleBlocks.RED_EGG_BLOCK);
		dropSelf(MubbleBlocks.PINK_EGG_BLOCK);
		dropSelf(MubbleBlocks.BLACK_EGG_BLOCK);
		dropSelf(MubbleBlocks.WHITE_EGG_BLOCK);
	}
}