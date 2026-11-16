package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.world.power_up.ability.LaunchAbility;

import java.util.Optional;

/**
 * An entity that can be boosted straight up on a double tap of its jump key, granted by its power-up.
 *
 * @see LaunchAbility
 */
public interface Launching {
    default Optional<LaunchAbility> getLaunchAbility() {
        return Optional.empty();
    }

    /** Whether the holder is on the way up of a launch. */
    default boolean isLaunching() {
        return false;
    }

    /** Whether the holder is on the slow descent that follows the peak of a launch. */
    default boolean isSlowFalling() {
        return false;
    }

    /** Whether the launch of the current ground contact has been spent. */
    default boolean hasLaunched() {
        return false;
    }
}
