package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.minecraft.resources.ResourceKey;

public class PowerUpsKeys {
    public static final ResourceKey<PowerUp> MINI = createKey("mini");
    public static final ResourceKey<PowerUp> MEGA = createKey("mega");
    public static final ResourceKey<PowerUp> FIRE = createKey("fire");
    public static final ResourceKey<PowerUp> ICE = createKey("ice");

    private static ResourceKey<PowerUp> createKey(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP, Mubble.id(path));
    }
}
