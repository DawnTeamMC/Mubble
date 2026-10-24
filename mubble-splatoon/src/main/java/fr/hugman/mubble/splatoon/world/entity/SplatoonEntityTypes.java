package fr.hugman.mubble.splatoon.world.entity;

import fr.hugman.mubble.splatoon.references.SplatoonEntityTypeKeys;
import fr.hugman.mubble.splatoon.world.entity.projectile.InkDrop;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBullet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class SplatoonEntityTypes {
    /**
     * Sized after the radius bullets have against terrain in Splatoon 3. Clients fly bullets themselves, so the server
     * only corrects them now and then.
     */
    public static final EntityType<ShooterInkBullet> SHOOTER_INK_BULLET = of(SplatoonEntityTypeKeys.SHOOTER_INK_BULLET, EntityType.Builder.<ShooterInkBullet>of(ShooterInkBullet::new, MobCategory.MISC)
            .sized(0.4f, 0.4f)
            .eyeHeight(0.2f)
            .noSave()
            .clientTrackingRange(4)
            .updateInterval(5)
            .alwaysUpdateVelocity(true));
    public static final EntityType<InkDrop> INK_DROP = of(SplatoonEntityTypeKeys.INK_DROP, EntityType.Builder.<InkDrop>of(InkDrop::new, MobCategory.MISC)
            .sized(0.2f, 0.2f)
            .eyeHeight(0.1f)
            .noSave()
            .clientTrackingRange(4)
            .updateInterval(5)
            .alwaysUpdateVelocity(true));

    private static <T extends Entity> EntityType<T> of(ResourceKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(id));
    }
}
