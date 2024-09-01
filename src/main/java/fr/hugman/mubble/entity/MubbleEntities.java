package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class MubbleEntities {
    public static final EntityType<GoombaEntity> GOOMBA = Registry.register(
            Registries.ENTITY_TYPE,
            Mubble.id("goomba"),
            EntityType.Builder.create(GoombaEntity::new, SpawnGroup.CREATURE).dimensions(0.75f, 0.75f).build("cube")
    );

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(GOOMBA, GoombaEntity.createMobAttributes());
    }
}
