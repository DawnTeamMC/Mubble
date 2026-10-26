package fr.hugman.mubble.platformer.reward;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.platformer.PlatformerUnlocks;
import fr.hugman.mubble.platformer.access.MoveSelector;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

/**
 * Unlocks platformer moves for a player.
 * <p>
 * An unlock grants nothing by itself: it is what makes a source saying {@code enable} about the move
 * actually let the player use it.
 * <pre>{@code
 * { "type": "mubble:platformer_move", "moves": ["mubble:backflip", "#mubble:wall"] }
 * }</pre>
 *
 * @param moves the moves to unlock; a tag unlocks the moves it holds at the time of the reward
 */
public record PlatformerMoveReward(List<MoveSelector> moves) implements Reward {
    public static final MapCodec<PlatformerMoveReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.nonEmptyList(MoveSelector.CODEC.listOf()).fieldOf("moves").forGetter(PlatformerMoveReward::moves)
    ).apply(instance, PlatformerMoveReward::new));

    @Override
    public RewardType<?> type() {
        return PlatformerRewardTypes.PLATFORMER_MOVE;
    }

    @Override
    public void grant(ServerPlayer player) {
        this.moves.forEach(selector -> PlatformerUnlocks.unlock(player, selector));
    }
}
