package fr.hugman.mubble.entity;

import fr.hugman.mubble.entity.projectile.ShooterInkBulletEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MubbleEntityTypes {
    public static final EntityType<ShooterInkBulletEntity> SHOOTER_INK_BULLET = of(MubbleEntityTypeKeys.SHOOTER_INK_BULLET, EntityType.Builder.<ShooterInkBulletEntity>create(ShooterInkBulletEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .eyeHeight(0.25f)
            .maxTrackingRange(4)
            .trackingTickInterval(20)
            .alwaysUpdateVelocity(true));

    private static <T extends Entity> EntityType<T> of(RegistryKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, id, type.build(id.toString()));
    }
}
