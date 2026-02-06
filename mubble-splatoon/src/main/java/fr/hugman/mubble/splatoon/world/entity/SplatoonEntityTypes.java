package fr.hugman.mubble.splatoon.world.entity;

import fr.hugman.mubble.splatoon.references.SplatoonEntityTypeKeys;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBullet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class SplatoonEntityTypes {
    public static final EntityType<ShooterInkBullet> SHOOTER_INK_BULLET = of(SplatoonEntityTypeKeys.SHOOTER_INK_BULLET, EntityType.Builder.<ShooterInkBullet>of(ShooterInkBullet::new, MobCategory.MISC)
            .sized(0.5f, 0.5f)
            .eyeHeight(0.25f)
            .clientTrackingRange(4)
            .updateInterval(20)
            .alwaysUpdateVelocity(true));

    private static <T extends Entity> EntityType<T> of(ResourceKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(id));
    }
}
