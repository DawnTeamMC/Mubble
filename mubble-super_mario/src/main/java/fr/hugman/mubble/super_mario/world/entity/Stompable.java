package fr.hugman.mubble.super_mario.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents an entity that can be stomped (be jumped on).
 *
 * @author Hugman
 * @since v4.0.0
 */
public interface Stompable {
    /**
     * How hard a stomp throws whoever landed it back up, in blocks per tick, for an enemy with a full-grown
     * goomba's worth of give underfoot.
     */
    double DEFAULT_STOMP_BOUNCE = 0.5D;

    default boolean canBeStomped() {
        return false;
    }

    default AABB getStompBox() {
        return null;
    }

    default Predicate<? super Entity> getStompableBy() {
        return EntitySelector.NO_CREATIVE_OR_SPECTATOR;
    }

    /**
     * How hard stomping this throws the stomper back up, in blocks per tick. The smaller the enemy, the
     * less there is to push off.
     */
    default double getStompBounce() {
        return DEFAULT_STOMP_BOUNCE;
    }

    default void onStompedBy(Entity entity) {
    }
}
