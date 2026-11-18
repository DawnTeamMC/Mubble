package fr.hugman.mubble.world.entity;

import fr.hugman.mubble.world.power_up.ability.GroundPoundAbility;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * An entity that can ground pound, and turn to stone doing it, granted by its power-up.
 * <p>
 * When something else than vanilla moves the entity, it tells which one has the motion with
 * {@link #setGroundPoundDrivenElsewhere} and reports the two ends of each pound, so that the statue and the
 * impact stay the same whoever drives.
 *
 * @see GroundPoundAbility
 */
public interface GroundPounding {
    default Optional<GroundPoundAbility> getGroundPoundAbility() {
        return Optional.empty();
    }

    default boolean isGroundPounding() {
        return false;
    }

    default boolean isStatue() {
        return false;
    }

    /** Whether another mover than core drives the motion of the pound. */
    default void setGroundPoundDrivenElsewhere(boolean drivenElsewhere) {
    }

    /** Server side: a pound the mover drives has started. */
    default void startGroundPound() {
    }

    /** Server side: a pound the mover drives has landed, its feet at {@code feet}. The mover pounds the blocks itself. */
    default void landGroundPound(Vec3 feet) {
    }
}
