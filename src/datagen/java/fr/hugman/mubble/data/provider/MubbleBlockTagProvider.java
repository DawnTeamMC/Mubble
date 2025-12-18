package fr.hugman.mubble.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.block.MubbleBlocks.*;
import static fr.hugman.mubble.tag.MubbleBlockTags.*;

public class MubbleBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public MubbleBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(MELTABLE_TO_WATER).add(Blocks.ICE);
        valueLookupBuilder(MELTABLE_TO_ICE).add(Blocks.PACKED_ICE);
        valueLookupBuilder(FREEZABLE_TO_PACKED_ICE).add(Blocks.ICE);

		// SUPER MARIO
		valueLookupBuilder(BRICK_BLOCKS).add(BRICK_BLOCK, CRYSTAL_BLOCK, GOLD_BLOCK);
		valueLookupBuilder(EXCLAMATION_BLOCKS).add(
				BLUE_EXCLAMATION_BLOCK,
				GREEN_EXCLAMATION_BLOCK,
				YELLOW_EXCLAMATION_BLOCK,
				RED_EXCLAMATION_BLOCK
		);
		valueLookupBuilder(MARIMBA_BLOCKS).add(
				BLUE_MARIMBA_BLOCK,
				GREEN_MARIMBA_BLOCK,
				YELLOW_MARIMBA_BLOCK,
				RED_MARIMBA_BLOCK
		);
		valueLookupBuilder(SNAKE_BLOCKS).add(SNAKE_BLOCK, FAST_SNAKE_BLOCK, SLOW_SNAKE_BLOCK);
		valueLookupBuilder(BEEP_BLOCKS).add(RED_BEEP_BLOCK, BLUE_BEEP_BLOCK);

		// YOSHI'S ISLAND
		valueLookupBuilder(EGG_BLOCKS).add(
				BLUE_EGG_BLOCK,
				CYAN_EGG_BLOCK,
				GREEN_EGG_BLOCK,
				YELLOW_EGG_BLOCK,
				ORANGE_EGG_BLOCK,
				RED_EGG_BLOCK,
				PINK_EGG_BLOCK,
				BLACK_EGG_BLOCK,
				WHITE_EGG_BLOCK
		);

		// Vanilla
		valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL)
				.add(EMPTY_BLOCK)
				.add(QUESTION_BLOCK)
				.addTag(BRICK_BLOCKS)
				.addTag(EXCLAMATION_BLOCKS)
				.add(NOTE_BLOCK)
				.addTag(MARIMBA_BLOCKS)
				.addTag(SNAKE_BLOCKS)
				.addTag(BEEP_BLOCKS)
				.addTag(EGG_BLOCKS);
		valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(EMPTY_BLOCK)
				.add(QUESTION_BLOCK)
				.addTag(BRICK_BLOCKS)
				.addTag(EXCLAMATION_BLOCKS)
				.add(NOTE_BLOCK)
				.addTag(MARIMBA_BLOCKS)
				.addTag(SNAKE_BLOCKS)
				.addTag(BEEP_BLOCKS)
				.addTag(EGG_BLOCKS);
	}
}