package fr.hugman.mubble.core.registries;

import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import fr.hugman.mubble.world.voyage.trial.TrialDefinition;
import fr.hugman.mubble.world.voyage.waystation.WaystationDefinition;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class MubbleBuiltInRegistries {
    public static final Registry<PowerUpActionType<?>> POWER_UP_ACTION_TYPE = register(MubbleRegistries.POWER_UP_ACTION_TYPE);

    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.create(key).buildAndRegister();
    }

    public static void register() {
        // Datapack-authored, and the client cannot render a trial it has never heard of, so it has to
        // be synced. The separate network codec keeps the server-only half of a profile at home.
        DynamicRegistries.registerSynced(MubbleRegistries.ENVIRONMENT_PROFILE, EnvironmentProfile.DIRECT_CODEC, EnvironmentProfile.NETWORK_CODEC);

        // Trials and voyages stay server-side. Everything the client needs to render a trial
        // already reaches it as an environment profile plus a resolved override, and a definition
        // holds seeds and future loot tables that a client has no business reading. Phase 4 wants the
        // display names for the command; that is a payload, not a reason to sync the whole registry.
        DynamicRegistries.register(MubbleRegistries.TRIAL, TrialDefinition.DIRECT_CODEC);
        DynamicRegistries.register(MubbleRegistries.WAYSTATION, WaystationDefinition.DIRECT_CODEC);
        DynamicRegistries.register(MubbleRegistries.VOYAGE, VoyageDefinition.DIRECT_CODEC);

        DynamicRegistries.registerSynced(MubbleRegistries.POWER_UP, PowerUp.DIRECT_CODEC);
        DynamicRegistries.registerSynced(MubbleRegistries.POWER_UP_ACTION, PowerUpAction.TYPE_CODEC);
    }
}
