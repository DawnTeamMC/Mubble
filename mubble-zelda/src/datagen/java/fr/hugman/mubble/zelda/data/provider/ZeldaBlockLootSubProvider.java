package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.zelda.world.level.block.ZeldaBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ZeldaBlockLootSubProvider extends FabricBlockLootSubProvider {
    public ZeldaBlockLootSubProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Break an egg with anything but silk touch and there is nothing left to pick up.
        dropWhenSilkTouch(ZeldaBlocks.LOFTWING_EGG);
        dropWhenSilkTouch(ZeldaBlocks.CRACKED_LOFTWING_EGG);
        dropWhenSilkTouch(ZeldaBlocks.HATCHING_LOFTWING_EGG);
    }
}
