package fr.hugman.mubble.arcade.reward;

import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;

/**
 * Something a player earns through the story: a quest, a trial, a cutscene hands it out.
 * <p>
 * Rewards are data, dispatched on their {@code type}. The story systems are still to come; until
 * then rewards are granted through code or commands, and the arcade movement unlocks are the first
 * ones.
 */
public interface Reward {
    Codec<Reward> CODEC = ArcadeBuiltInRegistries.REWARD_TYPE.byNameCodec().dispatch(Reward::type, RewardType::codec);

    RewardType<?> type();

    /** Hands the reward to {@code player}. */
    void grant(ServerPlayer player);

    record Type<R extends Reward>(MapCodec<R> codec) implements RewardType<R> {
    }
}
