package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.zelda.tags.ZeldaBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;

import java.util.concurrent.CompletableFuture;

public class ZeldaBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public ZeldaBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // Anything that puffs out spores when you hold a bottle to it.
        builder(ZeldaBlockTags.SPORE_SOURCES).add(
                BlockItemIds.BROWN_MUSHROOM,
                BlockItemIds.RED_MUSHROOM,
                BlockItemIds.CRIMSON_FUNGUS,
                BlockItemIds.WARPED_FUNGUS
        );
    }
}
