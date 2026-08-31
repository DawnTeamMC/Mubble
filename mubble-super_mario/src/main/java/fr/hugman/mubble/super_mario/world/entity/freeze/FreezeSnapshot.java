package fr.hugman.mubble.super_mario.world.entity.freeze;

/**
 * The pose an entity was caught in, kept on the entity itself for as long as the ice holds.
 * <p>
 * Every living entity is one of these, through the mixin on {@code LivingEntity}. Winding the render
 * state age back holds everything driven by it still, but the limbs are driven by the walk animation
 * instead, which keeps running down to a standstill however immobile the entity is — an entity frozen
 * mid-stride would ease into a resting pose over the next half second rather than hold the stride.
 * Reading the walk animation back from here instead is what makes the freeze a snapshot: it is taken
 * the moment the ice takes hold and never moves again.
 */
public interface FreezeSnapshot {
    /** @return how far into its walk cycle the entity was when it froze */
    float frozenWalkPos();

    /** @return how wide the entity was swinging its limbs when it froze */
    float frozenWalkSpeed();
}
