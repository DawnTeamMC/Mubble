package fr.hugman.mubble.world.reward;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import net.minecraft.core.Registry;

public class RewardTypes {
    /** Unlocks arcade moves, see {@link ArcadeMoveReward}. */
    public static final RewardType<ArcadeMoveReward> ARCADE_MOVE = register("arcade_move", ArcadeMoveReward.CODEC);

    private static <R extends Reward> RewardType<R> register(String path, MapCodec<R> codec) {
        return Registry.register(MubbleBuiltInRegistries.REWARD_TYPE, Mubble.id(path), new Reward.Type<>(codec));
    }
}
