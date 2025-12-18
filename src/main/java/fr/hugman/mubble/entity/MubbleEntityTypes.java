package fr.hugman.mubble.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class MubbleEntityTypes {
    public static final EntityType<GoombaEntity> GOOMBA = of(MubbleEntityTypeKeys.GOOMBA, EntityType.Builder.of(GoombaEntity::new, MobCategory.CREATURE).sized(0.6f, 0.755f).eyeHeight(0.53125f));
    public static final EntityType<GreenKoopaShellEntity> GREEN_KOOPA_SHELL = of(MubbleEntityTypeKeys.GREEN_KOOPA_SHELL, EntityType.Builder.<GreenKoopaShellEntity>of(GreenKoopaShellEntity::new, MobCategory.MISC).sized(10 / 16f, 7 / 16f));
    public static final EntityType<RedKoopaShellEntity> RED_KOOPA_SHELL = of(MubbleEntityTypeKeys.RED_KOOPA_SHELL, EntityType.Builder.<RedKoopaShellEntity>of(RedKoopaShellEntity::new, MobCategory.MISC).sized(10 / 16f, 7 / 16f));
    public static final EntityType<FireballEntity> FIREBALL = of(MubbleEntityTypeKeys.FIREBALL, EntityType.Builder.<FireballEntity>of(FireballEntity::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10));
    public static final EntityType<IceballEntity> ICEBALL = of(MubbleEntityTypeKeys.ICEBALL, EntityType.Builder.<IceballEntity>of(IceballEntity::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10));

    private static <T extends Entity> EntityType<T> of(ResourceKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type.build(id));
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(GOOMBA, GoombaEntity.createGoombaAttributes());
    }
}
