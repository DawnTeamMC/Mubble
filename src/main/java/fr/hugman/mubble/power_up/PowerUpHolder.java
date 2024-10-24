package fr.hugman.mubble.power_up;

import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public interface PowerUpHolder {
    default Optional<RegistryEntry<PowerUp>> getPowerUp() {
        return Optional.empty();
    }

    default void setPowerUp(RegistryEntry<PowerUp> powerUp) {
    }

    default void clearPowerUp() {
    }
}
