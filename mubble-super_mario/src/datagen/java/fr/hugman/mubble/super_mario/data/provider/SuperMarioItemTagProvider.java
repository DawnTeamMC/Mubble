package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.tags.SuperMarioBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.super_mario.references.SuperMarioBlockItemIds.*;
import static fr.hugman.mubble.super_mario.tags.SuperMarioItemTags.*;
import static fr.hugman.mubble.super_mario.references.SuperMarioItemIds.*;

public class SuperMarioItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
	public SuperMarioItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, @Nullable BlockTagsProvider blockTagProvider) {
		super(output, registriesFuture, blockTagProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		builder(COINS).add(COIN, RED_COIN, BLUE_COIN, FLOWER_COIN);
		builder(KOOPA_SHELLS).add(GREEN_KOOPA_SHELL, RED_KOOPA_SHELL);
		builder(SPAWNS_AS_COLLECTIBLE).addTag(COINS);
		builder(ItemTags.PIGLIN_LOVED).add(COIN);

		copy(SuperMarioBlockTags.BRICK_BLOCKS, BRICK_BLOCKS);
		copy(SuperMarioBlockTags.EXCLAMATION_BLOCKS, EXCLAMATION_BLOCKS);
		copy(SuperMarioBlockTags.MARIMBA_BLOCKS, MARIMBA_BLOCKS);
		copy(SuperMarioBlockTags.SNAKE_BLOCKS, SNAKE_BLOCKS);
		copy(SuperMarioBlockTags.BEEP_BLOCKS, BEEP_BLOCKS);

		copy(SuperMarioBlockTags.EGG_BLOCKS, EGG_BLOCKS);

		builder(ItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY)
				.addTag(EGG_BLOCKS);
		builder(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT)
				.addTag(BEEP_BLOCKS);
		builder(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
				.add(EMPTY_BLOCK)
				.add(QUESTION_BLOCK)
				.addTag(EXCLAMATION_BLOCKS);
		builder(ItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR)
				.addTag(BRICK_BLOCKS)
				.add(NOTE_BLOCK)
				.addTag(MARIMBA_BLOCKS)
				.addTag(SNAKE_BLOCKS);
	}
}