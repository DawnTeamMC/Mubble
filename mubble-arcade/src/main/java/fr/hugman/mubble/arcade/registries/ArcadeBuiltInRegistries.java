package fr.hugman.mubble.arcade.registries;

import fr.hugman.mubble.arcade.ArcadeProfile;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.reward.RewardType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;

public class ArcadeBuiltInRegistries {
    /**
     * The moves of the arcade movement layer. They are code: what a move does each tick is a state of
     * the movement state machine, and only its numbers come from data, through the profiles.
     */
    public static final Registry<ArcadeMove> ARCADE_MOVE = FabricRegistryBuilder.create(ArcadeRegistries.ARCADE_MOVE).attribute(RegistryAttribute.SYNCED).buildAndRegister();
    /** What the story hands out. The arcade move unlocks are the first rewards; the story systems will add theirs. */
    public static final Registry<RewardType<?>> REWARD_TYPE = FabricRegistryBuilder.create(ArcadeRegistries.REWARD_TYPE).buildAndRegister();

    public static void register() {
        DynamicRegistries.registerSynced(ArcadeRegistries.ARCADE_PROFILE, ArcadeProfile.DIRECT_CODEC);
    }
}
