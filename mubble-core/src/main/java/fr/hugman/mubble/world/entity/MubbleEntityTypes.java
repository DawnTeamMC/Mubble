package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.references.MubbleEntityTypeKeys;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class MubbleEntityTypes {
    public static final EntityType<CollectibleEntity> COLLECTIBLE = of(MubbleEntityTypeKeys.COLLECTIBLE, EntityType.Builder.<CollectibleEntity>of(CollectibleEntity::new, MobCategory.MISC)
			.noLootTable()
			.sized(0.75F, 0.75F)
			.clientTrackingRange(6)
			.updateInterval(20));

    private static <T extends Entity> EntityType<T> of(ResourceKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(id));
    }
}
