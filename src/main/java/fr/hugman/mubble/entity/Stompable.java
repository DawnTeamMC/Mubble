package fr.hugman.mubble.entity;

import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents an entity that can be stomped (be jumped on).
 *
 * @author Hugman
 * @since v4.0.0
 */
public interface Stompable {
    default boolean canBeStomped() {
        return false;
    }

    default Box getStompBox() {
        return null;
    }

    default Predicate<? super Entity> getStompableBy() {
        return EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR;
    }

    default void onStompedBy(List<Entity> entities) {
    }
}
