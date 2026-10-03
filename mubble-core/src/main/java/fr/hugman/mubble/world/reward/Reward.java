package fr.hugman.mubble.world.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;

/**
 * Something a player earns through the story: a quest, a trial, a cutscene hands it out.
 * <p>
 * Rewards are data, dispatched on their {@code type}. The story systems are still to come; until
 * then rewards are granted through code or commands, and the arcade movement unlocks are the first
 * ones.
 */
public interface Reward {
    Codec<Reward> CODEC = MubbleBuiltInRegistries.REWARD_TYPE.byNameCodec().dispatch(Reward::type, RewardType::codec);

    RewardType<?> type();

    /** Hands the reward to {@code player}. */
    void grant(ServerPlayer player);

    record Type<R extends Reward>(MapCodec<R> codec) implements RewardType<R> {
    }
}
