package fr.hugman.mubble.core.registries;

import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import fr.hugman.mubble.world.reward.RewardType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class MubbleBuiltInRegistries {
    public static final Registry<PowerUpActionType<?>> POWER_UP_ACTION_TYPE = register(MubbleRegistries.POWER_UP_ACTION_TYPE);
    /**
     * The moves of the arcade movement layer. They are code: what a move does each tick is a state of
     * the movement state machine, and only its numbers come from data, through the profiles.
     */
    public static final Registry<ArcadeMove> ARCADE_MOVE = FabricRegistryBuilder.create(MubbleRegistries.ARCADE_MOVE).attribute(RegistryAttribute.SYNCED).buildAndRegister();
    public static final Registry<RewardType<?>> REWARD_TYPE = register(MubbleRegistries.REWARD_TYPE);

    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.create(key).buildAndRegister();
    }

    public static void register() {
        DynamicRegistries.registerSynced(MubbleRegistries.POWER_UP, PowerUp.DIRECT_CODEC);
        DynamicRegistries.registerSynced(MubbleRegistries.POWER_UP_ACTION, PowerUpAction.TYPE_CODEC);
        DynamicRegistries.registerSynced(MubbleRegistries.ARCADE_PROFILE, ArcadeProfile.DIRECT_CODEC);
    }
}
