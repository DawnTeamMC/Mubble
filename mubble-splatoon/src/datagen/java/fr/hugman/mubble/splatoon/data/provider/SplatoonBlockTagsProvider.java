package fr.hugman.mubble.splatoon.data.provider;

import fr.hugman.mubble.splatoon.tags.SplatoonBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class SplatoonBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public SplatoonBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // ink floating in the air would give invisible blocks away
        builder(SplatoonBlockTags.UNINKABLE).add(BuiltInRegistries.BLOCK.getResourceKey(Blocks.BARRIER).orElseThrow());
    }
}
