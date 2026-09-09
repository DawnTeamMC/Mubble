package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.resources.ResourceKey;

public class SuperMarioPowerUpIds {
    public static final ResourceKey<PowerUp> MINI = createKey("mini");
    public static final ResourceKey<PowerUp> MEGA = createKey("mega");
    public static final ResourceKey<PowerUp> FIRE = createKey("fire");
    public static final ResourceKey<PowerUp> ICE = createKey("ice");
    public static final ResourceKey<PowerUp> GOLD = createKey("gold");
    public static final ResourceKey<PowerUp> CLOUD = createKey("cloud");
    public static final ResourceKey<PowerUp> BUBBLE = createKey("bubble");
    public static final ResourceKey<PowerUp> FLOWER = createKey("flower");

    private static ResourceKey<PowerUp> createKey(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP, SuperMario.id(path));
    }
}
