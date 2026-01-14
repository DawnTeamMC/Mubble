package fr.hugman.mubble.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.world.item.MubbleItems.*;
import static fr.hugman.mubble.tags.MubbleItemTags.*;

public class MubbleItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
	public MubbleItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, @Nullable BlockTagsProvider blockTagProvider) {
		super(output, registriesFuture, blockTagProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		valueLookupBuilder(COINS).add(COIN, RED_COIN, BLUE_COIN, FLOWER_COIN);
		valueLookupBuilder(KOOPA_SHELLS).add(GREEN_KOOPA_SHELL, RED_KOOPA_SHELL);
		valueLookupBuilder(ItemTags.PIGLIN_LOVED).add(COIN);
	}
}