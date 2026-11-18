package fr.hugman.mubble.world.power_up;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
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

    static boolean holdsTagged(Entity entity, TagKey<PowerUp> tag) {
        return entity instanceof PowerUpHolder holder && holder.getPowerUp().map(powerUp -> powerUp.is(tag)).orElse(false);
    }
}
