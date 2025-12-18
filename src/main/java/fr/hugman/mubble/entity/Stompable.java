package fr.hugman.mubble.entity;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;

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

    default AABB getStompBox() {
        return null;
    }

    default Predicate<? super Entity> getStompableBy() {
        return EntitySelector.NO_CREATIVE_OR_SPECTATOR;
    }

    default void onStompedBy(List<Entity> entities) {
    }
}
