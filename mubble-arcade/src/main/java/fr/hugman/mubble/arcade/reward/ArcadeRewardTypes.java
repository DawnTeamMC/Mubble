package fr.hugman.mubble.arcade.reward;

import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.Mubble;
import net.minecraft.core.Registry;

public class ArcadeRewardTypes {
    /** Unlocks arcade moves, see {@link ArcadeMoveReward}. */
    public static final RewardType<ArcadeMoveReward> ARCADE_MOVE = register("arcade_move", ArcadeMoveReward.CODEC);

    private static <R extends Reward> RewardType<R> register(String path, MapCodec<R> codec) {
        return Registry.register(ArcadeBuiltInRegistries.REWARD_TYPE, Mubble.id(path), new Reward.Type<>(codec));
    }
}
