package fr.hugman.mubble.platformer.reward;

import fr.hugman.mubble.platformer.registries.PlatformerBuiltInRegistries;
import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.Mubble;
import net.minecraft.core.Registry;

public class PlatformerRewardTypes {
    /** Unlocks platformer moves, see {@link PlatformerMoveReward}. */
    public static final RewardType<PlatformerMoveReward> PLATFORMER_MOVE = register("platformer_move", PlatformerMoveReward.CODEC);

    private static <R extends Reward> RewardType<R> register(String path, MapCodec<R> codec) {
        return Registry.register(PlatformerBuiltInRegistries.REWARD_TYPE, Mubble.id(path), new Reward.Type<>(codec));
    }
}
