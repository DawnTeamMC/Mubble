package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.super_mario.world.level.storage.loot.SuperMarioBuiltInLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Loot tables rolled from a gameplay event rather than from a kill or a broken block: the only context they get
 * is the entity involved and where it happened.
 */
public class SuperMarioGameplayLootSubProvider extends SimpleFabricLootTableSubProvider {
	public SuperMarioGameplayLootSubProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		super(output, registryLookupFuture, LootContextParamSets.GIFT);
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		// Data packs can branch on the swallowed entity from here; a plain coin is the default for everything.
		output.accept(SuperMarioBuiltInLootTables.BUBBLE_CAPTURE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(SuperMarioItems.COIN))
				));
	}
}
