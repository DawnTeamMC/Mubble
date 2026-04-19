package fr.hugman.mubble.super_mario.world.entity;

import fr.hugman.mubble.super_mario.references.SuperMarioEntityTypeIds;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.Goomba;
import fr.hugman.mubble.super_mario.world.entity.platform.CloudPlatform;
import fr.hugman.mubble.super_mario.world.entity.projectile.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class SuperMarioEntityTypes {
    public static final EntityType<Goomba> GOOMBA = register(SuperMarioEntityTypeIds.GOOMBA, EntityType.Builder.of(Goomba::new, MobCategory.CREATURE).sized(0.6f, 0.755f).eyeHeight(0.53125f));
    public static final EntityType<GreenKoopaShell> GREEN_KOOPA_SHELL = register(SuperMarioEntityTypeIds.GREEN_KOOPA_SHELL, EntityType.Builder.<GreenKoopaShell>of(GreenKoopaShell::new, MobCategory.MISC).sized(10 / 16f, 7 / 16f));
    public static final EntityType<RedKoopaShell> RED_KOOPA_SHELL = register(SuperMarioEntityTypeIds.RED_KOOPA_SHELL, EntityType.Builder.<RedKoopaShell>of(RedKoopaShell::new, MobCategory.MISC).sized(10 / 16f, 7 / 16f));
    public static final EntityType<Fireball> FIREBALL = register(SuperMarioEntityTypeIds.FIREBALL, EntityType.Builder.<Fireball>of(Fireball::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10));
    public static final EntityType<Iceball> ICEBALL = register(SuperMarioEntityTypeIds.ICEBALL, EntityType.Builder.<Iceball>of(Iceball::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10));
    public static final EntityType<GoldFireball> GOLD_FIREBALL = register(SuperMarioEntityTypeIds.GOLD_FIREBALL, EntityType.Builder.<GoldFireball>of(GoldFireball::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10));
    public static final EntityType<CloudPlatform> CLOUD_PLATFORM = register(SuperMarioEntityTypeIds.CLOUD_PLATFORM, EntityType.Builder.of(CloudPlatform::new, MobCategory.MISC).sized(4.0F, 1.0F).clientTrackingRange(10));

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(id));
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(GOOMBA, Goomba.createGoombaAttributes());
    }
}
