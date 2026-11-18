package fr.hugman.mubble.platformer.registries;

import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.reward.RewardType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;

public class PlatformerBuiltInRegistries {
    /**
     * The moves of the platformer movement layer. They are code: what a move does each tick is a state of
     * the movement state machine, and only its numbers come from data, through the profiles.
     */
    public static final Registry<PlatformerMove> PLATFORMER_MOVE = FabricRegistryBuilder.create(PlatformerRegistries.PLATFORMER_MOVE).attribute(RegistryAttribute.SYNCED).buildAndRegister();
    /** What the story hands out. The platformer move unlocks are the first rewards; the story systems will add theirs. */
    public static final Registry<RewardType<?>> REWARD_TYPE = FabricRegistryBuilder.create(PlatformerRegistries.REWARD_TYPE).buildAndRegister();

    public static void register() {
        DynamicRegistries.registerSynced(PlatformerRegistries.PLATFORMER_PROFILE, PlatformerProfile.DIRECT_CODEC);
    }
}
