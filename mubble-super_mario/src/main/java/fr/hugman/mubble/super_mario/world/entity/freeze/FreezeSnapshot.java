package fr.hugman.mubble.super_mario.world.entity.freeze;

/**
 * The pose an entity was caught in, implemented by every living entity through the mixin on
 * {@code LivingEntity}. The walk animation keeps running down to a standstill however immobile the
 * entity is, so the limbs are read back from here rather than from it.
 */
public interface FreezeSnapshot {
    /** @return how far into its walk cycle the entity was when it froze */
    float frozenWalkPos();

    /** @return how wide the entity was swinging its limbs when it froze */
    float frozenWalkSpeed();
}
