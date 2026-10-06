package fr.hugman.mubble.testmod.references;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.testmod.MubbleTestMod;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.resources.ResourceKey;

public class TestModPowerUpIds {
    public static final ResourceKey<PowerUp> SNOWBALL = createKey("snowball");

    private static ResourceKey<PowerUp> createKey(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP, MubbleTestMod.id(path));
    }
}
