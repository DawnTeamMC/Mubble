package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.resources.ResourceKey;

public class PowerUpKeys {
    public static final ResourceKey<PowerUp> MINI = createKey("mini");
    public static final ResourceKey<PowerUp> MEGA = createKey("mega");
    public static final ResourceKey<PowerUp> FIRE = createKey("fire");
    public static final ResourceKey<PowerUp> ICE = createKey("ice");
    public static final ResourceKey<PowerUp> GOLD = createKey("gold");

    private static ResourceKey<PowerUp> createKey(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP, Mubble.id(path));
    }
}
