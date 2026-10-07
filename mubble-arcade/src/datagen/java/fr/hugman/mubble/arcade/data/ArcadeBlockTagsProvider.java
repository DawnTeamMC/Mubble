package fr.hugman.mubble.arcade.data;

import static fr.hugman.mubble.arcade.tags.ArcadeBlockTags.*;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockTags;

public class ArcadeBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
	public ArcadeBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// level design of the arcade movement: ice is too slippery to kick off, and carries speed along
		builder(NO_WALL_JUMP).add(BlockItemIds.ICE, BlockItemIds.PACKED_ICE, BlockItemIds.BLUE_ICE);
		builder(NO_LEDGE_GRAB).addOptionalTag(BlockTags.LEAVES);
		builder(BOUNCE).add(BlockItemIds.SLIME_BLOCK);
		builder(KEEPS_MOMENTUM).add(BlockItemIds.ICE, BlockItemIds.PACKED_ICE, BlockItemIds.BLUE_ICE);
	}
}
