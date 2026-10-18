package fr.hugman.mubble.zelda.world.level.storage.loot;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ZeldaBuiltInLootTables {
	/** What a loftwing comes back up with after dropping onto open water. */
	public static final ResourceKey<LootTable> LOFTWING_FISHING = register("gameplay/loftwing_fishing");

	private static ResourceKey<LootTable> register(final String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, Zelda.id(path));
	}
}
