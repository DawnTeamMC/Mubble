package fr.hugman.mubble.super_mario.tags;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SuperMarioEntityTypeTags {
    public static final TagKey<EntityType<?>> KOOPA_SHELLS = bind("koopa_shells");

    public static final TagKey<EntityType<?>> CAN_STOMP = bind("can_stomp");
    public static final TagKey<EntityType<?>> STOMPABLE = bind("stompable");

    /**
     * Entities an ice ball cannot trap at all, and only hurts.
     * <p>
     * Everything else is judged on its bulk alone: see {@link
     * fr.hugman.mubble.super_mario.world.entity.freeze.Freezing#isBig}.
     */
    public static final TagKey<EntityType<?>> FREEZE_IMMUNE = bind("freeze_immune");

    public static final TagKey<EntityType<?>> ALL = bind("all");
    public static final TagKey<EntityType<?>> BUBBLE_CAN_TRAP = bind("bubble_can_trap");
    public static final TagKey<EntityType<?>> BUBBLE_CANNOT_TRAP = bind("bubble_cannot_trap");

    private static TagKey<EntityType<?>> bind(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, SuperMario.id(path));
    }
}
