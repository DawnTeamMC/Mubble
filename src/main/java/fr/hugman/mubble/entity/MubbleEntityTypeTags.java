package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class MubbleEntityTypeTags {
    public static final TagKey<EntityType<?>> CAN_JUMP_BUMP = of("can_jump_bump");

    private static TagKey<EntityType<?>> of(String path) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Mubble.id(path));
    }
}
