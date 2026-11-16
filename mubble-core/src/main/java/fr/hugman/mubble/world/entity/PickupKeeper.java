package fr.hugman.mubble.world.entity;

import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

/**
 * A vehicle that keeps what rides it from being picked up, by players and mobs alike.
 */
public interface PickupKeeper {
    /**
     * @param taker who is trying, or {@code null} when the game does not say
     */
    boolean keepsFromPickup(Entity passenger, @Nullable Entity taker);

    static boolean isKeptFrom(Entity entity, @Nullable Entity taker) {
        return entity.getVehicle() instanceof PickupKeeper keeper && keeper.keepsFromPickup(entity, taker);
    }
}
