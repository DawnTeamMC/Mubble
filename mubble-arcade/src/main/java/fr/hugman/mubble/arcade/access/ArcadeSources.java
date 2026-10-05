package fr.hugman.mubble.arcade.access;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.arcade.ArcadeAttachments;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * The sources a player carries besides their equipment: trial rulesets, Vessels, commands.
 * <p>
 * They are kept on the player, saved with them and synced to their client, which needs them to
 * predict its own movement. Equipment is not stored here: it is read from the slots every tick, see
 * {@link ArcadeAccess#equipmentSources}.
 */
public record ArcadeSources(List<ArcadeSource> sources) {
    public static final ArcadeSources NONE = new ArcadeSources(List.of());
    public static final Codec<ArcadeSources> CODEC = ArcadeSource.CODEC.listOf().xmap(ArcadeSources::new, ArcadeSources::sources);
    public static final StreamCodec<ByteBuf, ArcadeSources> STREAM_CODEC = ArcadeSource.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(ArcadeSources::new, ArcadeSources::sources);

    public static ArcadeSources get(Player player) {
        return player.getAttachedOrElse(ArcadeAttachments.SOURCES, NONE);
    }

    public Optional<ArcadeSource> find(Identifier id) {
        return this.sources.stream().filter(source -> source.id().equals(id)).findFirst();
    }

    /** Adds {@code source} to the player, replacing the one with the same id. */
    public static void add(Player player, ArcadeSource source) {
        var sources = new ArrayList<>(get(player).sources);
        sources.removeIf(existing -> existing.id().equals(source.id()));
        sources.add(source);
        player.setAttached(ArcadeAttachments.SOURCES, new ArcadeSources(List.copyOf(sources)));
    }

    /** @return whether the player had a source with that id */
    public static boolean remove(Player player, Identifier id) {
        var current = get(player);
        if (current.find(id).isEmpty()) {
            return false;
        }
        var sources = new ArrayList<>(current.sources);
        sources.removeIf(existing -> existing.id().equals(id));
        if (sources.isEmpty()) {
            player.removeAttached(ArcadeAttachments.SOURCES);
        } else {
            player.setAttached(ArcadeAttachments.SOURCES, new ArcadeSources(List.copyOf(sources)));
        }
        return true;
    }

    /** Drops the sources past their expiry. Only the server does it, and the change reaches the client like any other. */
    public static void pruneExpired(Player player, long gameTime) {
        var current = get(player);
        if (current.sources.stream().noneMatch(source -> source.isExpired(gameTime))) {
            return;
        }
        var sources = current.sources.stream().filter(source -> !source.isExpired(gameTime)).toList();
        if (sources.isEmpty()) {
            player.removeAttached(ArcadeAttachments.SOURCES);
        } else {
            player.setAttached(ArcadeAttachments.SOURCES, new ArcadeSources(sources));
        }
    }
}
