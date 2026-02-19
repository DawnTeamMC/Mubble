package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.minecraft.resources.ResourceKey;

public class PowerUpActionTypesKeys {
    public static final ResourceKey<PowerUpActionType<?>> SHOOT_PROJECTILE = createKey("shoot_projectile");

    private static ResourceKey<PowerUpActionType<?>> createKey(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP_ACTION_TYPE, Mubble.id(path));
    }
}