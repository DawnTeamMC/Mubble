package fr.hugman.mubble.core.registries;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import fr.hugman.mubble.world.voyage.trial.TrialDefinition;
import fr.hugman.mubble.world.voyage.waystation.WaystationDefinition;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class MubbleRegistries {
    public static final ResourceKey<Registry<EnvironmentProfile>> ENVIRONMENT_PROFILE = createRegistryKey("environment_profile");
    public static final ResourceKey<Registry<TrialDefinition>> TRIAL = createRegistryKey("trial");
    public static final ResourceKey<Registry<WaystationDefinition>> WAYSTATION = createRegistryKey("waystation");
    public static final ResourceKey<Registry<VoyageDefinition>> VOYAGE = createRegistryKey("voyage");
    public static final ResourceKey<Registry<PowerUp>> POWER_UP = createRegistryKey("power_up");
    public static final ResourceKey<Registry<PowerUpActionType<?>>> POWER_UP_ACTION_TYPE = createRegistryKey("power_up_action_type");
    public static final ResourceKey<Registry<PowerUpAction>> POWER_UP_ACTION = createRegistryKey("power_up_action");

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(Mubble.id(path));
    }
}
