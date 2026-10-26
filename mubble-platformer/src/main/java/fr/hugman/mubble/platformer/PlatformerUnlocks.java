package fr.hugman.mubble.platformer;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import com.mojang.serialization.Codec;
import fr.hugman.mubble.platformer.access.MoveSelector;
import fr.hugman.mubble.platformer.move.PlatformerMove;
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
public record PlatformerUnlocks(Set<ResourceKey<PlatformerMove>> moves) {
    public static final PlatformerUnlocks NONE = new PlatformerUnlocks(Set.of());
    public static final Codec<PlatformerUnlocks> CODEC = ResourceKey.codec(PlatformerRegistries.PLATFORMER_MOVE).listOf()
            .xmap(list -> new PlatformerUnlocks(Set.copyOf(new LinkedHashSet<>(list))), unlocks -> unlocks.moves.stream().sorted(java.util.Comparator.comparing(key -> key.identifier().toString())).toList());
    public static final StreamCodec<ByteBuf, PlatformerUnlocks> STREAM_CODEC = ResourceKey.streamCodec(PlatformerRegistries.PLATFORMER_MOVE)
            .apply(ByteBufCodecs.collection(HashSet::new))
            .map(set -> new PlatformerUnlocks(Set.copyOf(set)), unlocks -> new HashSet<>(unlocks.moves));

    public static PlatformerUnlocks get(Player player) {
        return player.getAttachedOrElse(PlatformerAttachments.UNLOCKS, NONE);
    }

    public boolean owns(PlatformerMove move) {
        return this.moves.contains(move.accessRoot().key());
    }

    /** Unlocks every move {@code selector} covers. */
    public static int unlock(Player player, MoveSelector selector) {
        var moves = new HashSet<>(get(player).moves);
        int before = moves.size();
        selector.forEachMove(move -> moves.add(move.accessRoot().key()));
        player.setAttached(PlatformerAttachments.UNLOCKS, new PlatformerUnlocks(Set.copyOf(moves)));
        return moves.size() - before;
    }

    /** Takes back every move {@code selector} covers. */
    public static int lock(Player player, MoveSelector selector) {
        var moves = new HashSet<>(get(player).moves);
        int before = moves.size();
        selector.forEachMove(move -> moves.remove(move.accessRoot().key()));
        player.setAttached(PlatformerAttachments.UNLOCKS, new PlatformerUnlocks(Set.copyOf(moves)));
        return before - moves.size();
    }
}
