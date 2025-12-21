package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.minecraft.resources.ResourceKey;

public class PowerUps {
    public static final ResourceKey<PowerUp> MINI = of("mini");
    public static final ResourceKey<PowerUp> MEGA = of("mega");
    public static final ResourceKey<PowerUp> FIRE = of("fire");
    public static final ResourceKey<PowerUp> ICE = of("ice");

    private static ResourceKey<PowerUp> of(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP, Mubble.id(path));
    }
}
