package fr.hugman.mubble.world.power_up;

import java.util.Optional;
import net.minecraft.core.Holder;
import org.jspecify.annotations.Nullable;

public interface PowerUpHolder {
    default Optional<Holder<PowerUp>> getPowerUp() {
        return Optional.empty();
    }

    default void setPowerUp(Holder<PowerUp> powerUp) {
    }

    default void clearPowerUp() {
    }

    @Nullable
    default PowerUpProperties getPowerUpProperties() {
        return null;
    }

    default void setPowerUpProperties(@Nullable PowerUpProperties properties) {}
}
