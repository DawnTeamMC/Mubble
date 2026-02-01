package fr.hugman.mubble.world.power_up;

import java.util.Optional;
import net.minecraft.core.Holder;

public interface PowerUpHolder {
    default Optional<Holder<PowerUp>> getPowerUp() {
        return Optional.empty();
    }

    default void setPowerUp(Holder<PowerUp> powerUp) {
    }

    default void clearPowerUp() {
    }

    default PowerUpProperties getPowerUpProperties() {
        return null;
    }
}
