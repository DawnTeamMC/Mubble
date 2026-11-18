package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.world.power_up.ability.FlightAbility;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/** An entity that flies on its jump key for as long as a gauge of the power-up it holds lasts. */
public interface Flying {
    default Optional<FlightAbility> getFlightAbility() {
        return Optional.empty();
    }

    default boolean isFlying() {
        return false;
    }

    /** Whether the flight under way is in the last ticks of its gauge. */
    default boolean isFlightSputtering() {
        return false;
    }

    /** The ticks of flight left, 0 without a flight ability. */
    default int getFlightGauge() {
        return 0;
    }

    /** Puts back what the flight ability prices {@code source} at: {@link FlightAbility#GROUND}, or one of another mod. */
    default void refillFlight(Identifier source) {
    }
}
