package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class SuperMarioBlockLootSubProvider extends FabricBlockLootSubProvider {
    public SuperMarioBlockLootSubProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(SuperMarioBlocks.EMPTY_BLOCK);
        dropSelf(SuperMarioBlocks.QUESTION_BLOCK);
        dropSelf(SuperMarioBlocks.BRICK_BLOCK);
        dropSelf(SuperMarioBlocks.CRYSTAL_BLOCK);
        dropSelf(SuperMarioBlocks.GOLD_BLOCK);
        dropSelf(SuperMarioBlocks.BLUE_EXCLAMATION_BLOCK);
        dropSelf(SuperMarioBlocks.GREEN_EXCLAMATION_BLOCK);
        dropSelf(SuperMarioBlocks.YELLOW_EXCLAMATION_BLOCK);
        dropSelf(SuperMarioBlocks.RED_EXCLAMATION_BLOCK);
        dropSelf(SuperMarioBlocks.NOTE_BLOCK);
        dropSelf(SuperMarioBlocks.BLUE_MARIMBA_BLOCK);
        dropSelf(SuperMarioBlocks.GREEN_MARIMBA_BLOCK);
        dropSelf(SuperMarioBlocks.YELLOW_MARIMBA_BLOCK);
        dropSelf(SuperMarioBlocks.RED_MARIMBA_BLOCK);
        dropSelf(SuperMarioBlocks.SNAKE_BLOCK);
        dropSelf(SuperMarioBlocks.FAST_SNAKE_BLOCK);
        dropSelf(SuperMarioBlocks.SLOW_SNAKE_BLOCK);
        dropSelf(SuperMarioBlocks.RED_BEEP_BLOCK);
        dropSelf(SuperMarioBlocks.BLUE_BEEP_BLOCK);

        dropSelf(SuperMarioBlocks.BLUE_EGG_BLOCK);
        dropSelf(SuperMarioBlocks.CYAN_EGG_BLOCK);
        dropSelf(SuperMarioBlocks.GREEN_EGG_BLOCK);
        dropSelf(SuperMarioBlocks.YELLOW_EGG_BLOCK);
        dropSelf(SuperMarioBlocks.ORANGE_EGG_BLOCK);
        dropSelf(SuperMarioBlocks.RED_EGG_BLOCK);
        dropSelf(SuperMarioBlocks.PINK_EGG_BLOCK);
        dropSelf(SuperMarioBlocks.BLACK_EGG_BLOCK);
        dropSelf(SuperMarioBlocks.WHITE_EGG_BLOCK);
    }
}