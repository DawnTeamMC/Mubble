package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.zelda.world.level.storage.loot.ZeldaBuiltInLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Loot rolled from something a loftwing did rather than from a kill or a broken block: the only
 * context it gets is the bird and where it happened.
 */
public class ZeldaGameplayLootSubProvider extends SimpleFabricLootTableSubProvider {
	public ZeldaGameplayLootSubProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		super(output, registryLookupFuture, LootContextParamSets.GIFT);
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		// Cod most of the time, salmon often, something prettier once in a while, and now and then
		// the bird comes up with nothing at all.
		output.accept(ZeldaBuiltInLootTables.LOFTWING_FISHING, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(Items.COD).setWeight(50))
						.add(LootItem.lootTableItem(Items.SALMON).setWeight(25))
						.add(LootItem.lootTableItem(Items.TROPICAL_FISH).setWeight(5))
						.add(LootItem.lootTableItem(Items.PUFFERFISH).setWeight(2))
						.add(EmptyLootItem.emptyItem().setWeight(18))
				));
	}
}
