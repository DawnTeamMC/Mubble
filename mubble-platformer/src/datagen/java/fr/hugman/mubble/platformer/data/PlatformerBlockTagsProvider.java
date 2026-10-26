package fr.hugman.mubble.platformer.data;

import static fr.hugman.mubble.platformer.tags.PlatformerBlockTags.*;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

public class PlatformerBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
	public PlatformerBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// slippery blocks, going by their friction, already keep momentum and refuse wall jumps: these tags only force it either way
		builder(NO_WALL_JUMP);
		builder(NO_LEDGE_GRAB).addOptionalTag(BlockTags.LEAVES);
		builder(KEEPS_MOMENTUM);
		builder(NOT_SLIPPERY);
	}
}
