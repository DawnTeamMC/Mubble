package fr.hugman.mubble.super_mario.data.provider;

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
		valueLookupBuilder(SPAWNS_AS_COLLECTIBLE).addTag(COINS);
		valueLookupBuilder(ItemTags.PIGLIN_LOVED).add(COIN);
	}
}