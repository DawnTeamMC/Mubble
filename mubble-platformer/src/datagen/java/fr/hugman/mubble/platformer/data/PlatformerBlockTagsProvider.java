package fr.hugman.mubble.platformer.data;

import static fr.hugman.mubble.platformer.tags.PlatformerBlockTags.*;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockTags;

public class PlatformerBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
	public PlatformerBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// level design of the platformer movement: ice is too slippery to kick off, and carries speed along
		builder(NO_WALL_JUMP).add(BlockItemIds.ICE, BlockItemIds.PACKED_ICE, BlockItemIds.BLUE_ICE);
		builder(NO_LEDGE_GRAB).addOptionalTag(BlockTags.LEAVES);
		builder(KEEPS_MOMENTUM).add(BlockItemIds.ICE, BlockItemIds.PACKED_ICE, BlockItemIds.BLUE_ICE);
	}
}
