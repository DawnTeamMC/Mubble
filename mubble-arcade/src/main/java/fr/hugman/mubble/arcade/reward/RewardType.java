package fr.hugman.mubble.arcade.reward;

import com.mojang.serialization.MapCodec;

/**
 * A kind of {@link Reward}, registered in {@code mubble:reward_type}.
 */
public interface RewardType<R extends Reward> {
    MapCodec<R> codec();
}
