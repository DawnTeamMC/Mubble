package fr.hugman.mubble.zelda.world.entity;

import fr.hugman.mubble.zelda.references.ZeldaEntityTypeIds;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ZeldaEntityTypes {
	// The standing size: in flight and in panic the bird reshapes itself, see Loftwing#getDefaultDimensions.
	public static final EntityType<Loftwing> LOFTWING = register(ZeldaEntityTypeIds.LOFTWING, EntityType.Builder.of(Loftwing::new, MobCategory.CREATURE).sized(1.5F, 2.5F).eyeHeight(2.2F).clientTrackingRange(12));

	private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> id, EntityType.Builder<T> type) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(id));
	}

	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(LOFTWING, Loftwing.createAttributes());
	}

	public static void registerSpawnPlacements() {
		SpawnPlacements.register(LOFTWING, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Loftwing::checkLoftwingSpawnRules);
	}
}
