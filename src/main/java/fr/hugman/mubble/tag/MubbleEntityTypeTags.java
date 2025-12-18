package fr.hugman.mubble.tag;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class MubbleEntityTypeTags {
    public static final TagKey<EntityType<?>> KOOPA_SHELLS = of("koopa_shells");

    public static final TagKey<EntityType<?>> CAN_STOMP = of("can_stomp");
    public static final TagKey<EntityType<?>> STOMPABLE = of("stompable");

    private static TagKey<EntityType<?>> of(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Mubble.id(path));
    }
}
