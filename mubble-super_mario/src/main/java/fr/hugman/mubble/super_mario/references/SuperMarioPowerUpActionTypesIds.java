package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.minecraft.resources.ResourceKey;

public class SuperMarioPowerUpActionTypesIds {
    public static final ResourceKey<PowerUpActionType<?>> SPAWN_CLOUD_PLATFORM = createKey("spawn_cloud_platform");

    private static ResourceKey<PowerUpActionType<?>> createKey(String path) {
        return ResourceKey.create(MubbleRegistries.POWER_UP_ACTION_TYPE, SuperMario.id(path));
    }
}