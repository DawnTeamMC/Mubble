package fr.hugman.mubble.splatoon.data.provider;

import fr.hugman.mubble.splatoon.world.item.SplatoonItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class SplatoonModelProvider extends FabricModelProvider {
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    public SplatoonModelProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output);
        this.registriesFuture = registriesFuture;
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(SplatoonItems.SPLATTERSHOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SplatoonItems.DOT_96_GAL, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(SplatoonItems.TEST_SHOOTER, ModelTemplates.FLAT_ITEM);
    }
}
