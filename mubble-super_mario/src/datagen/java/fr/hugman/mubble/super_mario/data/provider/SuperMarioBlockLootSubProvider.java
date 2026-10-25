package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

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
        dropSnakeBlock(SuperMarioBlocks.SNAKE_BLOCK);
        dropSnakeBlock(SuperMarioBlocks.FAST_SNAKE_BLOCK);
        dropSnakeBlock(SuperMarioBlocks.SLOW_SNAKE_BLOCK);
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

    /**
     * A snake block drops itself, unless it is respawnable.
     * <p>
     * A respawnable snake is rebuilt whole every time it is destroyed, so anything it dropped on the
     * way would be a copy of a block that is about to come back: a parkour course made of them would
     * be an item farm.
     */
    private void dropSnakeBlock(Block block) {
        add(block, LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(block).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(SnakeBlock.RESPAWNABLE, false)))))));
    }
}
