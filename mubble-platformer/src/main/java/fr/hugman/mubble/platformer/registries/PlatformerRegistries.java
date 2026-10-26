package fr.hugman.mubble.platformer.registries;

import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.reward.RewardType;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class PlatformerRegistries {
    public static final ResourceKey<Registry<PlatformerProfile>> PLATFORMER_PROFILE = MubbleRegistries.createRegistryKey("platformer_profile");
    public static final ResourceKey<Registry<PlatformerMove>> PLATFORMER_MOVE = MubbleRegistries.createRegistryKey("platformer_move");
    public static final ResourceKey<Registry<RewardType<?>>> REWARD_TYPE = MubbleRegistries.createRegistryKey("reward_type");
}
