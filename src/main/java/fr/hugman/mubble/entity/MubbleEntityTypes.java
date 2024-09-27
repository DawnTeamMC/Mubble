package fr.hugman.mubble.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class MubbleEntityTypes {
    public static final EntityType<GoombaEntity> GOOMBA = of(MubbleEntityTypeKeys.GOOMBA, EntityType.Builder.create(GoombaEntity::new, SpawnGroup.CREATURE).dimensions(0.6f, 0.755f).eyeHeight(0.53125f));

    private static <T extends Entity> EntityType<T> of(RegistryKey<EntityType<?>> id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, id, type.build(id.toString()));
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(GOOMBA, GoombaEntity.createGoombaAttributes());
    }
}
