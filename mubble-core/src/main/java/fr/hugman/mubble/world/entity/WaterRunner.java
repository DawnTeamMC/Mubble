package fr.hugman.mubble.world.entity;

/**
 * An entity that the surface of the water can carry.
 * <p>
 * Holding a power-up tagged {@code mubble:can_run_on_water} is not enough on its own: the sprint has
 * to have started on the ground, and it is lost the moment the entity runs into a wall or goes under.
 * The state is kept here rather than worked out by the collision code, because whether the water is
 * solid depends on where the sprint comes from and not only on what the current tick looks like.
 *
 * @author Hugman
 * @since v4.0.0
 */
public interface WaterRunner {
    /** Whether the entity is currently carried by the surface of the water. */
    default boolean isRunningOnWater() {
        return false;
    }

    default void setRunningOnWater(boolean runningOnWater) {
    }
}
