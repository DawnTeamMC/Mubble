package fr.hugman.mubble.power_up;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.registry.RegistryKey;

public class MubblePowerUps {
    public static final RegistryKey<PowerUp> MINI = of("mini");
    public static final RegistryKey<PowerUp> MEGA = of("mega");
    public static final RegistryKey<PowerUp> FIRE = of("fire");

    private static RegistryKey<PowerUp> of(String path) {
        return RegistryKey.of(MubbleRegistryKeys.POWER_UP, Mubble.id(path));
    }
}
