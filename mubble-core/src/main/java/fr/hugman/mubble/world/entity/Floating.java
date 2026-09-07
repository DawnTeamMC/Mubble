package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.world.power_up.ability.FloatAbility;

import java.util.Optional;

/**
 * An entity that can come down slowly by leaning on its jump key, granted by whichever power-up it holds.
 * <p>
 * Injected onto {@code Player}, and deliberately apart from {@link Fluttering}: a form is free to grant the
 * float without the climb that usually comes before it.
 *
 * @see FloatAbility
 */
public interface Floating {
    /**
     * @return the float the currently held power-up grants, if it grants one at all
     */
    default Optional<FloatAbility> getFloatAbility() {
        return Optional.empty();
    }

    /**
     * @return whether the holder is being held to a float right now
     */
    default boolean isFloating() {
        return false;
    }
}
