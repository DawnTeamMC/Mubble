package fr.hugman.mubble.power_up.action;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.minecraft.resources.ResourceKey;

public class PowerUpActionTypeKeys {
    public static final ResourceKey<PowerUpActionType<?>> SHOOT_PROJECTILE = of("shoot_projectile");

    private static ResourceKey<PowerUpActionType<?>> of(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP_ACTION_TYPE, Mubble.id(path));
    }
}