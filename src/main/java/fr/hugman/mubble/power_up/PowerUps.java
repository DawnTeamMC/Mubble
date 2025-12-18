package fr.hugman.mubble.power_up;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.resources.ResourceKey;

public class PowerUps {
    public static final ResourceKey<PowerUp> MINI = of("mini");
    public static final ResourceKey<PowerUp> MEGA = of("mega");
    public static final ResourceKey<PowerUp> FIRE = of("fire");
    public static final ResourceKey<PowerUp> ICE = of("ice");

    private static ResourceKey<PowerUp> of(String path) {
        return ResourceKey.create(MubbleRegistryKeys.POWER_UP, Mubble.id(path));
    }
}
