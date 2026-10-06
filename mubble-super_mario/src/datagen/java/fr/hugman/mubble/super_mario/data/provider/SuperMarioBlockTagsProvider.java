package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.references.SuperMarioBlockItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.super_mario.tags.SuperMarioBlockTags.*;
import static fr.hugman.mubble.super_mario.references.SuperMarioBlockItemIds.*;

public class SuperMarioBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public SuperMarioBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // Mod
        builder(BRICK_BLOCKS).add(SuperMarioBlockItemIds.BRICK_BLOCK, CRYSTAL_BLOCK, GOLD_BLOCK);
        builder(EXCLAMATION_BLOCKS).add(
                BLUE_EXCLAMATION_BLOCK,
                GREEN_EXCLAMATION_BLOCK,
                YELLOW_EXCLAMATION_BLOCK,
                RED_EXCLAMATION_BLOCK
        );
        builder(MARIMBA_BLOCKS).add(
                BLUE_MARIMBA_BLOCK,
                GREEN_MARIMBA_BLOCK,
                YELLOW_MARIMBA_BLOCK,
                RED_MARIMBA_BLOCK
        );
        builder(SNAKE_BLOCKS).add(SNAKE_BLOCK, FAST_SNAKE_BLOCK, SLOW_SNAKE_BLOCK);
        builder(BEEP_BLOCKS).add(RED_BEEP_BLOCK, BLUE_BEEP_BLOCK);

        builder(GOLD_EXPLOSION_SENSITIVE).add(BRICK_BLOCK, CRYSTAL_BLOCK);

        builder(EGG_BLOCKS).add(
                BLUE_EGG_BLOCK,
                CYAN_EGG_BLOCK,
                GREEN_EGG_BLOCK,
                YELLOW_EGG_BLOCK,
                ORANGE_EGG_BLOCK,
                RED_EGG_BLOCK,
                PINK_EGG_BLOCK,
                BLACK_EGG_BLOCK,
                WHITE_EGG_BLOCK
        );

        // Vanilla
        builder(BlockTags.NEEDS_STONE_TOOL)
                .add(EMPTY_BLOCK)
                .add(QUESTION_BLOCK)
                .addTag(BRICK_BLOCKS)
                .addTag(EXCLAMATION_BLOCKS)
                .add(NOTE_BLOCK)
                .addTag(MARIMBA_BLOCKS)
                .addTag(SNAKE_BLOCKS)
                .addTag(BEEP_BLOCKS)
                .addTag(EGG_BLOCKS);
        builder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(EMPTY_BLOCK)
                .add(QUESTION_BLOCK)
                .addTag(BRICK_BLOCKS)
                .addTag(EXCLAMATION_BLOCKS)
                .add(NOTE_BLOCK)
                .addTag(MARIMBA_BLOCKS)
                .addTag(SNAKE_BLOCKS)
                .addTag(BEEP_BLOCKS)
                .addTag(EGG_BLOCKS);
    }
}