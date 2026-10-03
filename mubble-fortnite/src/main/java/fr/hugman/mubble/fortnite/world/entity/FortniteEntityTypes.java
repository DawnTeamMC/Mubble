package fr.hugman.mubble.fortnite.world.entity;

import fr.hugman.mubble.fortnite.references.FortniteEntityTypeIds;
import fr.hugman.mubble.fortnite.world.entity.projectile.ImpulseGrenade;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class FortniteEntityTypes {
    // A grenade changes heading on every bounce, so it is synced far more often than a projectile that flies straight.
    public static final EntityType<ImpulseGrenade> IMPULSE_GRENADE = register(FortniteEntityTypeIds.IMPULSE_GRENADE, EntityType.Builder.<ImpulseGrenade>of(ImpulseGrenade::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(2));

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(id));
    }
}
