package fr.hugman.mubble.zelda.world.level.biome;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class ZeldaBiomeModifications {
	private static final Identifier LOFTWINGS = Zelda.id("loftwings");

	private static final int LOFTWING_SPAWN_WEIGHT = 10;
	private static final int LOFTWING_MIN_GROUP = 1;
	private static final int LOFTWING_MAX_GROUP = 2;

	/**
	 * Loftwings belong to every overworld biome, because what keeps them rare is not where they
	 * spawn but how high: the spawn rule turns down anything below the clouds, so in practice they
	 * only turn up on mountain tops and above whatever the player has built up there.
	 */
	public static void register() {
		BiomeModifications.create(LOFTWINGS).add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), (selectionContext, modificationContext) ->
				modificationContext.getMobSpawnSettings().addSpawn(
						MobCategory.CREATURE,
						new MobSpawnSettings.SpawnerData(ZeldaEntityTypes.LOFTWING, LOFTWING_MIN_GROUP, LOFTWING_MAX_GROUP),
						LOFTWING_SPAWN_WEIGHT
				));
	}
}
