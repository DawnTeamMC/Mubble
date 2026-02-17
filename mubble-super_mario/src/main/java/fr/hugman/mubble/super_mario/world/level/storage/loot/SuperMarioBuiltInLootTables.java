package fr.hugman.mubble.super_mario.world.level.storage.loot;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class SuperMarioBuiltInLootTables {
	public static final ResourceKey<LootTable> GOLDEN_KILL = register("golden_kill");

	private static ResourceKey<LootTable> register(final String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, SuperMario.id(path));
	}
}
