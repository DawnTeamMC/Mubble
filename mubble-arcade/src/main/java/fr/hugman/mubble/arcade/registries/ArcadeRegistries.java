package fr.hugman.mubble.arcade.registries;

import fr.hugman.mubble.arcade.ArcadeProfile;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.reward.RewardType;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ArcadeRegistries {
    public static final ResourceKey<Registry<ArcadeProfile>> ARCADE_PROFILE = MubbleRegistries.createRegistryKey("arcade_profile");
    public static final ResourceKey<Registry<ArcadeMove>> ARCADE_MOVE = MubbleRegistries.createRegistryKey("arcade_move");
    public static final ResourceKey<Registry<RewardType<?>>> REWARD_TYPE = MubbleRegistries.createRegistryKey("reward_type");
}
