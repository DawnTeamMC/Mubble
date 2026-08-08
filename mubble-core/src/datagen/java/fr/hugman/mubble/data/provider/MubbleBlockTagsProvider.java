package fr.hugman.mubble.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.tags.MubbleBlockTags.*;

public class MubbleBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
	public MubbleBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		builder(MELTABLE_TO_WATER).add(BlockItemIds.ICE);
		builder(MELTABLE_TO_ICE).add(BlockItemIds.PACKED_ICE);
		builder(FREEZABLE_TO_PACKED_ICE).add(BlockItemIds.ICE);
	}
}