package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class MubbleEntityTypeTags {
    public static final TagKey<EntityType<?>> CAN_STOMP = of("can_stomp");
    public static final TagKey<EntityType<?>> STOMPABLE = of("stompable");

    private static TagKey<EntityType<?>> of(String path) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Mubble.id(path));
    }
}
