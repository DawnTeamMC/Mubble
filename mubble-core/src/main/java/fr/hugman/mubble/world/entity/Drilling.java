package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.world.power_up.ability.DrillAbility;

import java.util.Optional;

/**
 * An entity that can drill straight down from the air, granted by its power-up.
 *
 * @see DrillAbility
 */
public interface Drilling {
    default Optional<DrillAbility> getDrillAbility() {
        return Optional.empty();
    }

    default boolean isDrilling() {
        return false;
    }
}
