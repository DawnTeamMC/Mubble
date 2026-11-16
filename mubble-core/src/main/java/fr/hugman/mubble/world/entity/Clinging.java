package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.world.power_up.ability.ClingAbility;

import java.util.Optional;

/** An entity that climbs some blocks like a ladder, as the power-up it holds allows. */
public interface Clinging {
    default Optional<ClingAbility> getClingAbility() {
        return Optional.empty();
    }

    default boolean isClinging() {
        return false;
    }
}
