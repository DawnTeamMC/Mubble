package fr.hugman.mubble.platformer.access;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.platformer.PlatformerAttachments;
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
 * {@link PlatformerAccess#equipmentSources}.
 */
public record PlatformerSources(List<PlatformerSource> sources) {
    public static final PlatformerSources NONE = new PlatformerSources(List.of());
    public static final Codec<PlatformerSources> CODEC = PlatformerSource.CODEC.listOf().xmap(PlatformerSources::new, PlatformerSources::sources);
    public static final StreamCodec<ByteBuf, PlatformerSources> STREAM_CODEC = PlatformerSource.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(PlatformerSources::new, PlatformerSources::sources);

    public static PlatformerSources get(Player player) {
        return player.getAttachedOrElse(PlatformerAttachments.SOURCES, NONE);
    }

    public Optional<PlatformerSource> find(Identifier id) {
        return this.sources.stream().filter(source -> source.id().equals(id)).findFirst();
    }

    /** Adds {@code source} to the player, replacing the one with the same id. */
    public static void add(Player player, PlatformerSource source) {
        var sources = new ArrayList<>(get(player).sources);
        sources.removeIf(existing -> existing.id().equals(source.id()));
        sources.add(source);
        player.setAttached(PlatformerAttachments.SOURCES, new PlatformerSources(List.copyOf(sources)));
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
            player.removeAttached(PlatformerAttachments.SOURCES);
        } else {
            player.setAttached(PlatformerAttachments.SOURCES, new PlatformerSources(List.copyOf(sources)));
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
            player.removeAttached(PlatformerAttachments.SOURCES);
        } else {
            player.setAttached(PlatformerAttachments.SOURCES, new PlatformerSources(sources));
        }
    }
}
