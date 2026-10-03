package fr.hugman.mubble.world.reward;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.arcade.ArcadeUnlocks;
import fr.hugman.mubble.world.arcade.access.MoveSelector;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

/**
 * Unlocks arcade moves for a player.
 * <p>
 * An unlock grants nothing by itself: it is what makes a source saying {@code enable} about the move
 * actually let the player use it.
 * <pre>{@code
 * { "type": "mubble:arcade_move", "moves": ["mubble:backflip", "#mubble:wall"] }
 * }</pre>
 *
 * @param moves the moves to unlock; a tag unlocks the moves it holds at the time of the reward
 */
public record ArcadeMoveReward(List<MoveSelector> moves) implements Reward {
    public static final MapCodec<ArcadeMoveReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.nonEmptyList(MoveSelector.CODEC.listOf()).fieldOf("moves").forGetter(ArcadeMoveReward::moves)
    ).apply(instance, ArcadeMoveReward::new));

    @Override
    public RewardType<?> type() {
        return RewardTypes.ARCADE_MOVE;
    }

    @Override
    public void grant(ServerPlayer player) {
        this.moves.forEach(selector -> ArcadeUnlocks.unlock(player, selector));
    }
}
