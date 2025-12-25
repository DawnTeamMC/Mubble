package fr.hugman.mubble.tags;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class MubbleEntityTypeTags {
    public static final TagKey<EntityType<?>> KOOPA_SHELLS = bind("koopa_shells");

    public static final TagKey<EntityType<?>> CAN_STOMP = bind("can_stomp");
    public static final TagKey<EntityType<?>> STOMPABLE = bind("stompable");

    private static TagKey<EntityType<?>> bind(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Mubble.id(path));
    }
}
