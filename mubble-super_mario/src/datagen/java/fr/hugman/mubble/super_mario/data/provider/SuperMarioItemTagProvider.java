package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.tags.SuperMarioBlockTags;
import fr.hugman.mubble.super_mario.tags.SuperMarioItemTags;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.super_mario.tags.SuperMarioItemTags.*;
import static fr.hugman.mubble.super_mario.world.item.SuperMarioItems.*;

public class SuperMarioItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
	public SuperMarioItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, @Nullable BlockTagsProvider blockTagProvider) {
		super(output, registriesFuture, blockTagProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		valueLookupBuilder(COINS).add(COIN, RED_COIN, BLUE_COIN, FLOWER_COIN);
		valueLookupBuilder(KOOPA_SHELLS).add(GREEN_KOOPA_SHELL, RED_KOOPA_SHELL);
		valueLookupBuilder(KOOPA_SHELLS).add(GREEN_KOOPA_SHELL, RED_KOOPA_SHELL);
		valueLookupBuilder(ItemTags.PIGLIN_LOVED).add(COIN);

		copy(SuperMarioBlockTags.BRICK_BLOCKS, BRICK_BLOCKS);
		copy(SuperMarioBlockTags.EXCLAMATION_BLOCKS, EXCLAMATION_BLOCKS);
		copy(SuperMarioBlockTags.MARIMBA_BLOCKS, MARIMBA_BLOCKS);
		copy(SuperMarioBlockTags.SNAKE_BLOCKS, SNAKE_BLOCKS);
		copy(SuperMarioBlockTags.BEEP_BLOCKS, BEEP_BLOCKS);

		copy(SuperMarioBlockTags.EGG_BLOCKS, EGG_BLOCKS);

		valueLookupBuilder(ItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY)
				.addTag(EGG_BLOCKS);
		valueLookupBuilder(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT)
				.addTag(BEEP_BLOCKS);
		valueLookupBuilder(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
				.add(SuperMarioBlocks.EMPTY_BLOCK.asItem())
				.add(SuperMarioBlocks.QUESTION_BLOCK.asItem())
				.addTag(EXCLAMATION_BLOCKS);
		valueLookupBuilder(ItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR)
				.addTag(BRICK_BLOCKS)
				.add(SuperMarioBlocks.NOTE_BLOCK.asItem())
				.addTag(MARIMBA_BLOCKS)
				.addTag(SNAKE_BLOCKS);
	}
}