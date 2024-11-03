package fr.hugman.mubble.power_up.action;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.registry.RegistryKey;

public class PowerUpActionTypeKeys {
    public static final RegistryKey<PowerUpActionType<?>> SHOOT_PROJECTILE = of("shoot_projectile");

    private static RegistryKey<PowerUpActionType<?>> of(String path) {
        return RegistryKey.of(MubbleRegistryKeys.POWER_UP_ACTION_TYPE, Mubble.id(path));
    }
}