package fr.hugman.mubble.super_mario.world.entity;

/**
 * An entity that can be handed a few blocks of free fall, spent on its next descent.
 *
 * @author Hugman
 * @since v4.0.0
 */
public interface FallGraced {
    /**
     * Grants blocks of fall that will not count towards fall damage on the way down.
     * <p>
     * The grace is held until the entity is actually falling again rather than taken off its fall distance
     * straight away: the server wipes a player's fall distance on every movement packet that gains height, so
     * anything written at the moment of a launch is gone by the next tick.
     *
     * @param blocks blocks of fall to forgive
     */
    default void grantFallGrace(double blocks) {
    }
}
