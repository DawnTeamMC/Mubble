package fr.hugman.mubble.arcade;

import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import com.mojang.serialization.Codec;
import fr.hugman.mubble.arcade.access.MoveSelector;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

/**
 * The moves a player owns, earned through the story.
 * <p>
 * Owning a move grants nothing by itself: it is what lets a source saying {@code enable} about it
 * actually make it available. Unlocks are kept by key, so that a move missing from a later version of
 * the game is simply ignored rather than lost.
 */
public record ArcadeUnlocks(Set<ResourceKey<ArcadeMove>> moves) {
    public static final ArcadeUnlocks NONE = new ArcadeUnlocks(Set.of());
    public static final Codec<ArcadeUnlocks> CODEC = ResourceKey.codec(ArcadeRegistries.ARCADE_MOVE).listOf()
            .xmap(list -> new ArcadeUnlocks(Set.copyOf(new LinkedHashSet<>(list))), unlocks -> unlocks.moves.stream().sorted(java.util.Comparator.comparing(key -> key.identifier().toString())).toList());
    public static final StreamCodec<ByteBuf, ArcadeUnlocks> STREAM_CODEC = ResourceKey.streamCodec(ArcadeRegistries.ARCADE_MOVE)
            .apply(ByteBufCodecs.collection(HashSet::new))
            .map(set -> new ArcadeUnlocks(Set.copyOf(set)), unlocks -> new HashSet<>(unlocks.moves));

    public static ArcadeUnlocks get(Player player) {
        return player.getAttachedOrElse(ArcadeAttachments.UNLOCKS, NONE);
    }

    public boolean owns(ArcadeMove move) {
        return this.moves.contains(move.accessRoot().key());
    }

    /** Unlocks every move {@code selector} covers. */
    public static int unlock(Player player, MoveSelector selector) {
        var moves = new HashSet<>(get(player).moves);
        int before = moves.size();
        selector.forEachMove(move -> moves.add(move.accessRoot().key()));
        player.setAttached(ArcadeAttachments.UNLOCKS, new ArcadeUnlocks(Set.copyOf(moves)));
        return moves.size() - before;
    }

    /** Takes back every move {@code selector} covers. */
    public static int lock(Player player, MoveSelector selector) {
        var moves = new HashSet<>(get(player).moves);
        int before = moves.size();
        selector.forEachMove(move -> moves.remove(move.accessRoot().key()));
        player.setAttached(ArcadeAttachments.UNLOCKS, new ArcadeUnlocks(Set.copyOf(moves)));
        return before - moves.size();
    }
}
